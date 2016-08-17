# DNA-Concatenation

Challenge:
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


Example input:
>Frag_56
ATTAGACCTG
>Frag_57
CCTGCCGGAA
>Frag_58
AGACCTGCCG
>Frag_59
GCCGGAATAC

Example output:
ATTAGACCTGCCGGAATAC
i.e (ATTAGACCTG, AGACCTGCCG, CCTGCCGGAA, GCCGGAATAC)  



~~~~~~~~~~~~~~~~~~~~~~~~~~
Approach for solving:
~~~~~~~~~~~~~~~~~~~~~~~~~~

Here are the broad steps:
 
1) Create a DNASequence case class 
2) Parse the data set file and deserialize lines into the DNASequence case class
3) Once we have a List of DNA sequence, construct the chromosome as follows:
    a) Recursively find right overlapping sequences that pass the 
       threshold and get the combined right stitched part
    b) Filter the used fragments
    c) Recursively apply left overlap stitching to the remaining fragments
    d) This would create the resultant stitched chromosome  


~~~~~~~~~~~~~~~~
Miscellaneous:
~~~~~~~~~~~~~~~~
There is a possibility that there could be a mutation in gene collection,
which would cause a noisy pattern in the gene, breaking the longest 
common subsequence match between two DNA sequences. To tackle this,
we can apply advanced string matching algorithms like Ratcliff-Obershelp
to ensure that noise does not affect the matching.