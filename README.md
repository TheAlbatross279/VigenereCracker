#Attacking the Vigenere cipher#

In order to attack the Vigenere cipher, it is necessary to disentangle the various alphabets, and to do this it is necessary to determine the key length.
Once the key length has been determined it is possible to attack individual alphabets using frequency analysis. Once an alphabet is known, letter frequency and n-gram frequency can be used to attack neighboring alphabets.

##Index of Coincidence##

The index of coincidence1 is defined to be the probability that two letters chosen at random from a given ciphertext are the same. If Fc is the frequency of cipher character c—the number of them encountered in the ciphertext—and N is the total number of ciphertext characters, the index of coincidence can be calculated as:

IC = (1/(N*(N-1)) * sum(Fi * (Fi-1)))

The expected value of IC for a cipher of length N with key length d generated from English plaintext can be computed by:
Exepcted_IC = (1/d)*((N-d)/(N-1))*(0.066) + ((d-1)/d)* (N/(N-1))*(0.038) 

##Kasiski##

Kasiski determined that repeated substrings in the ciphertext are likely to result from places where the same plaintext lined up at the same place in the key. (Otherwise, such coincidences are highly unlikely.) If this is true, the distance between repeated substrings will be a multiple of the key length.
To determine possible key lengths from repeated substrings, take all the distances you find between repeats, and look for common divisors. For example, if two strings appear 12 characters apart, key lengths of 1, 2, 3, 4, 6, and 12 are possible, because the prime factors of 12 are 1, 2, 2, and 3. If another pair of strings appears 16 characters apart (factors 1, 2, 2, 2, and 2) we know that the key length must be either 1, 2, or 4.

This is not foolproof. If there is a repeated substring in the key, there can be false positives, but it’s a place to start.


(Content written from asgn.pdf)
