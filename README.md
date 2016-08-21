# Chromosome-Builder

~~~~~~~~~~
Challenge:
~~~~~~~~~~

The input to the problem is at most 50 DNA sequences (i.e, the character
set is limited to T/C/G/A) whose length does not exceed 1000 
characters. The sequences are given in FASTA 
format (https://en.wikipedia.org/wiki/FASTA_format). These sequences 
are all different fragments of one chromosome.
The specific set of sequences you will get satisfy a very unique 
property:  there exists a unique way to reconstruct the entire 
chromosome from these reads by gluing together pairs of reads that 
overlap by more than half their length. An example set of input strings 
is attached.
The output of your program should be this unique sequence that contains 
each of the given input strings as a substring.


**Example input:**

\>Frag_56 

ATTAGACCTG
 
 
\>Frag_57
 
CCTGCCGGAA
 
 
\>Frag_58
 
AGACCTGCCG
 
 
\>Frag_59
 
GCCGGAATAC 


**Example output:**

~~ Reconstructed: Complete Chromosome ~~

Fragments:

Frag_56|Frag_58|Frag_57|Frag_59

Sequence:

ATTAGACCTGCCGGAATAC
  
~~~~~~~~~~~~~~~~~~~~~~~~~~
Approach for solving:
~~~~~~~~~~~~~~~~~~~~~~~~~~

Here are the broad steps:
  
1) Parse the data set file and deserialize lines into a list of dna sequences

2) Once we have a List of dna sequence, construct the chromosome as follows:

    a) get right stitch:
     
       Take a reference DNA sequence from the list and recursively 
       reconstruct the reference by iteratively joining a candidate dna 
       sequence from the remaining list which has the maximum overlap of 
       text in its beginning with the end portion of the reference of that iteration.
       
    b) Filter the used fragments
    
    c) get Left Stitch:
    
       Take the right stitch produced in a) as reference and remaining
       fragments as the list of dna sequences. Now, recursively reconstruct 
       this reference by iteratively joining a candidate dna sequence 
       from the remaining list which has the maximum overlap of 
       text in its ending with the beginning portion of the reference of that iteration.
       
    d) In case all dna sequences are utlized a Chromose is created, else
       a DNA Sequence is generated.

~~~~~~~~~~~~~~~~
Miscellaneous:
~~~~~~~~~~~~~~~~
There is a possibility that there could be a mutation in gene collection,
which would cause a noisy pattern in the gene, breaking the longest 
common subsequence match between two DNA sequences. To tackle this,
we might be able to use string matching algorithms like Ratcliff-Obershelp
to ensure that noise does not affect the matching.
