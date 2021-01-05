# Lab Mini-Project: MCTS

### Group number - P01 Group 7

### Authors

- Dumitras Diaconu 61064
- João Francês 61246
- João Pontes 62287

</br>

# Description of the problem and the used algorithm

The given problem was to implement the MCTS (Monte Carlo Tree Search) algorithm following the approach and design pattern presented in Lab tutorial 1 (Blocks World) and apply it to one of the following games: Blocks World or TicTacToe. We chose to apply it to TicTacToe. To do so, we first needed to create a class responsible for holding all of the information regarding a TicTacToe game. After that, we proceeded to create a MCTS implementation which can be used to play any board game that follows our approach and design pattern.

</br>

Speaking of MCTS in more detail, it is a probabilistic and heuristic driven search algorithm that combines the classic tree search implementations alongside machine learning principles of reinforcement learning. In tree search, there is always the possibility that the current best action is not always the most optimal action. This is where MCTS becomes useful as it periodically evaluates other alternatives during the learning phase by executing them. This is known as the **exploration-exploitation trade-off**, meaning that it exploits the actions and strategies that are found to be the best until now but also must continue to explore the local space of alternative decisions and find out if they could replace the current best.

</br>

The algorithm consists of four phases, which are repeated until the exhaustion of the given resources (i.e. iterations or time):

</br>

## Phase 1 - Selection

Using a tree policy, find the best node to start exploring, until we come across a node with unexplored children or the game is over.

The tree policy at node i is governed by the upper limit of the confidence interval of merit:

$$\frac{w_i}{n_i}+c\sqrt{\frac{\ln t}{n_i}}$$

where:

- $w_i$ is the number of wins scored in $i$;
- $n_i$ is the number of simulations counted in $i$;
- $t$ is the total number of simulations counted at the parent node of $i$;
- $c$ is a user-defined exploration parameter, theoretically equal to $\sqrt{2}$.

</br>

## Phase 2 - Expansion

If the current selected node has unexplored child nodes, select a child and add it to the decision tree.

</br>

## Phase 3 - Simulation

Simulate the game until the end from the currently selected node.

</br>

## Phase 4 - Backpropagation

Take the rewards received by each player at the end of the game and pass those values back up the tree to update all node statistics.

</br>

# All design options taken

Our first step was to create a TicTacToe class capable of holding information and playing moves in order to be able to play a full game by ourselves (player vs player).

Afterwards, it was time to implement the Monte Carlo Tree Search. At first, we believed following the exact implementation for LAB 1 (Blocks World) would be a good idea, more specifically using a static State class and implementing a solve function that could solve any given board.

However, we realized that we would begin to lose track of all the given states and would need a Tree class to be able to hold all of the states passed to it and keep track of all the updates.