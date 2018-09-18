/****************Manual*********************/

1. Compile the Server program- javac Server.java

2. Open four Client session

3. complie the four client sessions to connect to server - javac Client.java

4. player 1 & 3 are in TeamA and player 2 & 4 are in TeamB.

5. After 4 players are connected, cards are distributed to each client by the server.

6. Then current player asks to enter the bids

7.for first round , first player starts the game(i.e player1)

8. After each player enters the bids, the total bids for each team is calculated and displayed by server.

9. Each player selects the cards from their deck of 13 cards distributed, one after the other.

10. The player with highest card won the trick and the team bids won is updated.

11. Steps 9 and 10 are repeated for 13 times and the team bids won is updated and the total score is calculated.

12. If the total score for any of the team is >= 250 then the game is over the team with highest score won the game. Otherwise, next round is started with player2 starting the game  and steps 8, 9, 10 and 11 are repeated.

/***Execution_Steps***/

1. Compile Server.java
        javac Server.java
2. Open four new sessions for four clients

3. Compiling Client code
        session1: javac Client.java client1_name
        session2: javac Client.java client2_name
        session3: javac Client.java client3_name
        session4: javac Client.java client4_name

4. Client Side: After the clients are distributed, Enter number of bids in each client session.

5. Client Side: After the 4 four clients enter the number of bids, Select the card from the distributed cards in each client session one after the other.

6. After one round is completed (i.e, 13 tricks are played) again the cards are distributed to the clients and the game is continued till the any of the team reaches score >= 250 first.
