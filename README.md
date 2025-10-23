<h1>🌟 Algorithms for Inverse Reinforcement Learning</h1>

![GitHub contributors](https://img.shields.io/github/contributors/ivomarb/Inverse-Reinforcement-Learning?color=ffcc66&style=for-the-badge)
![GitHub stars](https://img.shields.io/github/stars/ivomarb/Inverse-Reinforcement-Learning?color=ffcc66&style=for-the-badge)
[![GitHub forks](https://img.shields.io/github/forks/ivomarb/Inverse-Reinforcement-Learning?style=for-the-badge)](https://github.com/ivomarb/Inverse-Reinforcement-Learning/network)
[![GitHub issues](https://img.shields.io/github/issues/ivomarb/Inverse-Reinforcement-Learning?color=ffcc66&style=for-the-badge)](https://github.com/ivomarb/Inverse-Reinforcement-Learning/issues)
[![GitHub license](https://img.shields.io/github/license/ivomarb/Inverse-Reinforcement-Learning?style=for-the-badge)](https://github.com/ivomarb/Inverse-Reinforcement-Learning/blob/master/LICENSE)
[![Twitter Follow](https://img.shields.io/twitter/follow/ivomarb?color=ffcc66&logo=twitter&logoColor=ffffff&style=for-the-badge)](https://twitter.com/ivomarbrito)


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#-gridworld-algorithms-implemented">Gridworld Algorithms Implemented</a>
    </li>
    <li>
      <a href="#-step-by-step-guide">Step by Step Guide</a>
    </li>
    <li>
      <a href="#-references">References</a>
    </li>
    <li>
      <a href="#-contributing">Contributing</a>
    </li>
    <li>
      <a href="#-license">License</a>
    </li>
  </ol>
</details>

This repo implements the following algorithms in a Java application with a GUI: 

- Value Iteration [[**1**](#References)]</br>
- Inverse Reinforcement Learning (IRL) [[**2**](#References)] </br> 

<p align="center">
<img src="images/gridworld.jpeg" alt="Banner" width="600">
</p>

It allows you define any gridworld environment similar to what you find in [[**2**](#References)] at _Section 5. IRL from Sampled Trajectories_. It allows you to define the received reward by the agent when it switches to the absorbing (goal) state of the grid as well as the reward received by the agent when it switches to a non-absorbing statte. You can define the following parameters of the IRL algorithm: RMax, Min and Max Lambada and Lambada Step. It will then output the predicted reward function as highlighted in the IRL algorithm. See section below _Step-by-step Guide_ on how to run this tool.

![rl](https://user-images.githubusercontent.com/33180566/32406065-176535da-c150-11e7-8a9b-107518775755.jpg)</br >
[Reinforcement Learning book](#References)

# 📖 Gridworld Algorithms Implemented

- Value Iteration [[**1**](#References)]</br >
- Inverse Reinforcement Learning [[**2**](#References)] </br > 

# ✨ Step by Step Guide

Gridworld experiment (Figures 1 and 2 of article)
Run class TRLMain.java or execute the JAR file: java -jar TRLMain-1.0.0.jar.

**Step 1**: Run TRLMain. A window with a standard 5x5 square gridworld appears.</br >
![irl1](https://user-images.githubusercontent.com/33180566/32405201-66584008-c13f-11e7-91a3-67b773d82e76.PNG)</br >
</br >
**Step 2**: Run action Create 2. Agent. The initial state is indicated by a blue square, the goal state is indicated by a blue circle. </br >
![irl2](https://user-images.githubusercontent.com/33180566/32405204-8fe0370a-c13f-11e7-82bc-c4c738cb3c3b.PNG)</br >
![irl3](https://user-images.githubusercontent.com/33180566/32405207-a54ff832-c13f-11e7-87d4-6e227b410ea1.PNG)</br >
</br >
**Step 3**: Run action Create 3. Reward Function. It will define the reward received when transition to the goal state (eg, positive value) and the reward received when transitioning to non absorbing states (eg, zero).</br >
![irl4](https://user-images.githubusercontent.com/33180566/32405210-cc15a9bc-c13f-11e7-9956-f09b802efbb3.PNG)</br >
![irl5](https://user-images.githubusercontent.com/33180566/32405223-0294540c-c140-11e7-93bf-573947df7c8d.PNG)</br >
</br >
**Step 4**: Run action Reinforcement Learning 1. Value Iteration. Finds an optimal policy, blue arrows. The red numbers are the Q-values for the actions in each state.</br >
![irl6](https://user-images.githubusercontent.com/33180566/32405227-23a66220-c140-11e7-8f61-2d42044e80da.PNG)</br >
</br >
**Step 5**: Run action Reinforcement Learning 2. Inverse Reinforcement Learning. Finds the reward function using Inverse Reinforcement Learning (IRL).</br >
![irl8](https://user-images.githubusercontent.com/33180566/32405973-478f4b4e-c14e-11e7-9e6f-8e72dafbbe4f.JPG)</br>
![irl7](https://user-images.githubusercontent.com/33180566/32405236-3e895fa2-c140-11e7-9dce-d7e0eae00fe1.PNG)</br >

# 🤝 Contributing

Contributions are welcome! Feel free to:

1. Fork the project
2. Create a branch for your feature (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

# 📚 References

[**1**] [Reinforcement Learning: An Introduction](http://incompleteideas.net/book/the-book-2nd.html), Richard S. Sutton and Andrew G. Barto.</br >
[**2**] [Algorithms for Inverse Reinforcement Learning](http://ai.stanford.edu/~ang/papers/icml00-irl.pdf). AY Ng, SJ Russell - Icml, 2000 - ai.stanford.edu.
[**3**] [SSparse Interactions in Multi-Agent Reinforcement Learning](https://ai.vub.ac.be/wp-content/uploads/2019/12/Sparse-Interactions-in-Multi-Agent-Reinforcement-Learning.pdf). Yann-Michaël De Hauwere, PhD Thesis, VUB, 2011.


# 📝 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

