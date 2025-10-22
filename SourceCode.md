# Source code overview — Inverse-Reinforcement-Learning

This document describes the Java sources under `src/`. The repository implements a Gridworld environment and two algorithms: Value Iteration (Reinforcement Learning) and Inverse Reinforcement Learning (IRL). The naming convention used here (provided by you) is:
- Interfaces start with `I` (e.g., `IRLGrid`).
- Abstract classes start with `A` (e.g., `ARLGrid`).
- Concrete classes start with `T` (e.g., `TRLGrid`).

I grouped the analysis by role: core interfaces and components, abstract base implementations, concrete RL/IRL implementations and GUI components. For each symbol I include responsibilities, important fields/methods, interactions, and notable implementation notes.

---

## High-level architecture

- IRL* and ARL* families define the domain model and base behavior for: Grid (`IRLGrid`/`ARLGrid`), Cell (`IRLCell`/`ARLCell`), State (`IRLState`/`ARLState`), Action (`IRLAction`/`ARLAction`), Wall (`IRLWall`/`ARLWall`), RewardFunction (`IRLRewardFunction`/`ARLRewardFunction`), TransitionProbability (`IRLTransitionProbability`/`ARLTransitionProbabilities`), Agent (`IRLAgent`/`ARLAgent`).
- TRL* classes implement concrete grid, cells, states, transitions, policies and GUI elements. They contain the logic that wires the gridworld, computes transition probability matrices, runs value iteration and IRL algorithms, and renders the GUI.
- The project uses Apache Commons Math (`RealMatrix`) for linear algebra / probability matrices.
- GUI components (panels and frames) are present: `TRLGridPanel`, `TRLMainFrame`, `TRLIRLRewardPanel`.

The main entry point is `TRLMain.java` which launches the application (or the jar as described in README).

---

## Interfaces (I*)

These interfaces define the public contracts used across the codebase.

- `IRLObject`
  - Purpose: marker/common constants holder for object types and action names used through the project.
  - Notable constants: `sCELL`, `sWALL`, `sGRID`, `sAGENT`, `sSTATE`, `sACTION_MOVE_NORTH`, `sACTION_MOVE_EAST`, `sACTION_MOVE_SOUTH`, `sACTION_MOVE_WEST`, `sPOLICY`, `sREWARD_FUNCTION`.

- `IRL` extends `IRLObject`
  - Marker interface. No members other than inheritance.

- `IRLGridComponent` extends `IRLObject`
  - Marker interface for components that belong to a grid (e.g., cells, walls).

- `IRLGrid`
  - Responsibilities: expose grid dimensions, cell list, reward function, and transition probability matrices for the four move actions.
  - Key methods:
    - `getCellList()` — returns all `IRLCell` instances.
    - `getNumberOfRows()/getNumberOfColumns()` and setters.
    - Accessors for transition probability matrices (`RealMatrix`) per action direction.
    - `getRewardFunction()/setRewardFunction(...)`.

- `IRLCell`
  - Responsibilities: define a cell API in the grid: index, coordinates, neighbors, walls.
  - Key methods: `getIndex()/setIndex`, `getRowIndex()/setRowIndex`, `getColumnIndex()/setColumnIndex`, `getNorthCell()/setNorthCell`, ..., `getNorthWall()/setNorthWall`, etc.
  - Also includes `isNeighboor(IRLCell)` (note: spelled "Neighboor") and `getWallCount()`.

- `IRLState`
  - Responsibilities: state abstraction wrapping a cell and set of available actions plus absorbing/initial flags.
  - Key methods: `getCell()/setCell`, `getActionList()`, `getAbsorbing()/setAbsorbing`, `getIndex()`, `getInitial()/setInitial`, and `retrieveAction(String aActionID)`.

- `IRLAction`
  - Simple contract for actions: `getID()/setID(String)`.

- `IRLWall`
  - Marker interface for wall objects (extends `IRLGridComponent`).

- `IRLAgent`
  - Responsibilities: agent container with pointer to grid, policy, state list, initial/absorbing states, discount factor and precomputed transition probability matrices.
  - Key methods: accessors for grid/policy/TP matrices and state retrieval by index or row/column.

- `IRLPolicy`
  - Represents learned/existing policy including Q-values and state-action pairs list.
  - Key methods: `getStateActionPairList()`, `getValueFunction()/setValueFunction(...)`, `getQValueHashMap()/setQValueHashMap(...)`.

- `IRLRewardFunction`
  - Responsibilities: mapping from `IRLState` to reward values and conversion to array.
  - Key methods: `getStateRewardHashMap()` and `toArray()`.

- `IRLTransitionProbability`
  - Marker interface (no members). Actual behavior is implemented in `ARLTransitionProbabilities` and concrete TRL transition classes.


## Abstract base classes (A*)

Abstract implementations provide default field storage, simple helpers and glue to the interfaces above.

- `ARLAction implements IRLAction`
  - Field: `fID`.
  - Default getter/setter for ID.

- `ARLAgent extends Observable implements IRLAgent`
  - Fields: grid, discount factor, policy, state list, initial and absorbing states, and four `RealMatrix` fields for TP matrices.
  - Methods:
    - `retrieveState(int index)` and `retrieveState(int row, int column)` — retrieve the associated `IRLState` from `fStateList`. Note: first method mistakenly indexes using `aIndex` inside loop but compares `lState.getCell().getIndex() == aIndex` (works if `getIndex()` equals loop index).
    - Accessors for policy, grid, TP matrices, initial/absorbing state, discounting factor with observable notifications when policy/absorbing state/grid changed.
  - Observability: `setPolicy` and `setAbsorbingState` call `setChanged()` and `notifyObservers()` to update GUI.

- `ARLCell implements IRLCell`
  - Fields: index, rowIndex, columnIndex, neighbor references (north/east/south/west), and wall references for each direction.
  - Implements all simple getters/setters.
  - `getWallCount()` returns the number of walls attached.

- `ARLGrid extends Observable implements IRLGrid`
  - Fields: number of rows/columns, cell list, reward function, and four `RealMatrix` fields for TP matrices.
  - Methods: basic getters/setters and observable notifications on dimension change.

- `ARLRewardFunction implements IRLRewardFunction`
  - Field: `HashMap<IRLState, Double> fStateRewardHashMap`.
  - `toArray()` converts the hashmap to an array indexed by `IRLState.getIndex()`.

- `ARLState implements IRLState`
  - Fields: `IRLCell fCell`, `List<IRLAction> fActionList`, booleans for absorbing and initial.
  - `retrieveAction(String)` iterates `fActionList` to return the matching action.
  - `getIndex()` delegates to `fCell.getIndex()`.

- `ARLTransitionProbabilities implements IRLTransitionProbability`
  - This class encapsulates the transition probability matrix creation algorithm shared by concrete transition implementations.
  - Abstract methods (to be implemented by concrete classes):
    - `getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithWallAtDirectionOfAction(IRLCell)`
    - `getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithoutWallAtDirectionOfAction(IRLCell)`
    - `getAgentStaysAtTheSameCellWithWallAtDirectionOfAction(IRLCell)`
    - `getDestinationCellIsNeighborInTheDirectionOfAction(IRLCell, IRLCell)`
    - `getDestinationCellIsNeighborNotInTheDirectionOfAction(IRLCell, IRLCell)`
  - `createTransitionProbabilitiesForAction(correctProb, noiseProb, agent)` builds a square matrix where each row is the origin state index and each column the destination. It contains the following behaviour:
    - If origin is absorbing state => 1 at diagonal.
    - If origin equals destination => compute probability based on walls and noise/correct action mix.
    - If destination not neighbor => 0.
    - If destination is neighbor: decide if it is the neighbor in the direction of the action (correct action) or not and set appropriate probability.
  - Uses `TRLGridUtil.getSharedInstance().retrieveCell(index, grid)` to map linear state indices to cells.
  - There are inconsistencies in naming like lNumberOfRows/lNumberOfColumns set to `lCellListSize` — this class assumes square layout or uses cell-count as both dims; it still iterates over cell list size, so it works but could be clearer.

- `ARLWall implements IRLWall`
  - Empty abstract; used as a base for any concrete wall.


## Concrete classes (T*) — TRL* (RL-specific concrete implementations)

These implement the actual grid, walls, cells, states, actions, transitions and GUI used by the application.

- Actions (movement):
  - `TRLActionMoveNorth`, `TRLActionMoveEast`, `TRLActionMoveSouth`, `TRLActionMoveWest`
  - They are concrete implementations (likely extend `ARLAction`) that provide IDs matching `IRLObject` constants (e.g. `sACTION_MOVE_NORTH`). (Files present in `src/` — content not listed above but named.)

- `TRLCell` — concrete cell implementation
  - Extends `ARLCell` — likely includes concrete neighbor wiring and `isNeighboor` implementation.

- `TRLState` — concrete state
  - Extends `ARLState` and pairs with `TRLCell`.

- `TRLWall` — extends `ARLWall`.

- `TRLGrid` — extends `ARLGrid`
  - Responsible for building the grid topology, indexing cells, wiring neighbors, and creating `RealMatrix` transition matrices by calling transition implementations.
  - Manages reward function assignment.

- `TRLAgent` — concrete agent
  - Extends `ARLAgent` and orchestrates running value iteration and IRL algorithms using the policy and transition matrices.

- `TRLTransitionProbabilitiesActionMoveNorth`, `TRLTransitionProbabilitiesActionMoveEast`, `TRLTransitionProbabilitiesActionMoveSouth`, `TRLTransitionProbabilitiesActionMoveWest`
  - Implement the abstract predicates in `ARLTransitionProbabilities` for the specific direction.
  - They decide when a destination cell counts as the neighbor in the action direction and calculate conditions for staying in origin when walls block movement.

- `TRLRewardFunction`, `TRLRewardFunctionUtil` and `TRLPolicy`, `TRLPolicyUtil` — utilities to hold and manipulate reward functions and policies.

- `TRLPolicy` holds state-action pairs (`TRLStateActionPair`) and Q-value hash map. `TRLPolicyUtil` contains helpers to compute policy from value function or Q-values.

- `TRLAgentUtil`, `TRLGridUtil`, `TRLIRLUtil`, `TRLRewardFunctionUtil` — helpers and singleton-like utilities used across classes for retrieving cells, converting indices, and executing parts of algorithms.

- `TRLGridPanel`, `TRLMainFrame`, `TRLIRLRewardPanel` — Swing UI components to display the grid, controls and reward visualizations. They observe the model (Observable) and update when notified.

- `TRLFactory` — likely builds the grid, agent, and default experiment setup.

- `TRLMain` — launches the application and constructs GUI via `TRLMainFrame`.


## Notable implementation details and observations

- Observability: `ARLGrid` and `ARLAgent` extend `Observable`. GUI classes register as observers to update rendering when the grid or policy/absorbing state changes.

- Transition probabilities:
  - Noise model: functions use `aCorrectActionProbability` and `aActionNoiseProbability` with the invariant correct+noise == 1. Probability mass of noise appears to be split among lateral moves (25% or 50% depending on walls), and when walls cause the agent to stay put, probabilities are added to stay at origin.
  - The implementation is tailored for gridworld with deterministic direction and rotational noise.

- Reward function representation:
  - `ARLRewardFunction` stores a HashMap keyed by `IRLState` to Double. `toArray()` uses `IRLState.getIndex()` as indices into the array — states must have continuous integer indices within [0..N-1].

- API naming and minor issues:
  - Typos in method/identifier names: `isNeighboor` (should be `isNeighbor`) and `getAgentStaysAtTheSameCellWithTwoSurroudingWallsWithWallAtDirectionOfAction` (surrouding -> surrounding) — these are cosmetic but consistent.
  - `ARLAgent.retrieveState(final int aIndex)` loops using lStateIndex but retrieves `fStateList.get(aIndex)` inside the loop; this is suspicious and might be a small bug — it still checks `lState.getCell().getIndex() == aIndex`, so it only returns if the matching state happens to be at that index in list; if not it falls through to assert false. The intended code likely should use `fStateList.get(lStateIndex)`.

- Build system: There is no Maven or Gradle in the repository; the code uses `org.apache.commons.math3` and `jfreechart` library jars present in `lib/` folder. The README shows the project is runnable via `java -jar TRLMain-1.0.0.jar` (a jar artifact not included in source tree here). Building is likely done with `javac` using the jars in classpath or via an IDE.


## How the algorithms map to the code (Value Iteration and IRL)

- Value Iteration
  - `TRLAgent` + `TRLPolicy` + `TRLRewardFunction` + transition matrices: value iteration updates the value function vector (`double[]`) and computes Q-values per (state,action) using the transition matrices and reward vector.
  - The policy stores a `valueFunction` and `QValueHashMap`; `TRLPolicyUtil` probably contains the value-iteration loop and policy extraction.

- Inverse Reinforcement Learning (IRL)
  - `TRLIRLUtil`, `TRLIRLRewardPanel`, `IRL` classes: the repository contains utilities to run the IRL algorithm described in Ng & Russell (2000). The IRL algorithm iterates over candidate reward functions (e.g., grid search over lambda) to find a reward that explains observed behavior (policy / trajectories).
  - `IRL` classes and `TRLI*` utilities manipulate reward vectors and use the same transition matrices and policy evaluation routines to compute expected feature counts and match observed expert trajectories.


## Files summary (by file)

- Interfaces (files under `src/`):
  - `IRL.java`, `IRLAction.java`, `IRLAgent.java`, `IRLCell.java`, `IRLGrid.java`, `IRLGridComponent.java`, `IRLObject.java`, `IRLPolicy.java`, `IRLRewardFunction.java`, `IRLState.java`, `IRLTransitionProbability.java`, `IRLWall.java`.

- Abstract classes (files starting with `A`):
  - `ARLAction.java` — base action with ID.
  - `ARLAgent.java` — base agent (observable) with grid, states and TP matrices.
  - `ARLCell.java` — base cell storing neighbors and walls.
  - `ARLGrid.java` — base grid storing cells and TP matrices.
  - `ARLRewardFunction.java` — base rewardfunction with state->reward hashmap and array conversion.
  - `ARLState.java` — base state with actions and absorbing/initial flags.
  - `ARLTransitionProbabilities.java` — base algorithm to create TP matrices for an action.
  - `ARLWall.java` — base wall.

- Concrete RL/TRL classes (files starting with `T`):
  - `TRLActionMoveEast.java`, `TRLActionMoveNorth.java`, `TRLActionMoveSouth.java`, `TRLActionMoveWest.java` — concrete actions for each direction.
  - `TRLAgent.java`, `TRLAgentUtil.java`, `TRLCell.java`, `TRLFactory.java`, `TRLGrid.java`, `TRLGridPanel.java`, `TRLGridUtil.java`, `TRLIRLRewardPanel.java`, `TRLIRLUtil.java`, `TRLMain.java`, `TRLMainFrame.java`, `TRLPolicy.java`, `TRLPolicyUtil.java`, `TRLRewardFunction.java`, `TRLRewardFunctionUtil.java`, `TRLState.java`, `TRLStateActionPair.java`, `TRLTransitionProbabilitiesActionMoveEast.java`, `TRLTransitionProbabilitiesActionMoveNorth.java`, `TRLTransitionProbabilitiesActionMoveSouth.java`, `TRLTransitionProbabilitiesActionMoveWest.java`, `TRLWall.java`.