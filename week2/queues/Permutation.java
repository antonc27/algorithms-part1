/* *****************************************************************************
 *  Name: Permutation
 *  Date: 26/10/19
 *  Description:
 *      Reads a sequence of strings from standard input using and prints exactly k of them, uniformly at random.
 *      Print each item from the sequence at most once.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();

        int k = Integer.parseInt(args[0]);

        while (StdIn.hasNextLine()) {
            String input = StdIn.readLine();
            for (String token : input.split(" ")) {
                rq.enqueue(token);
            }
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
