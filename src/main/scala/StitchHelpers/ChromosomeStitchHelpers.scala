package StitchHelpers

import Core.Utils.isCandidateQualified

/**
  * Created by siddharthachandra on 8/20/16.
  */
case class DNASequence(fragmentName: String, sequence: String)
case class Chromosome(fragmentName: String, sequence: String)

object ChromosomeStitchHelpers{

  /**
    * This combines a candidate DNA sequence that crossed the set threshold for left overlap
    * with the reference DNA sequence.
    * @param reference - DNA sequence to which the candidate DNA sequence is prepended
    * @param candidate - gets appended to reference DNA sequence
    * @param overlapLength - length of text that overlaps between the candidate and reference
    * @return - Combined DNA sequence from candidate and reference.
    *         Both Fragment name and DNA sequence of reference are appended to that of candidate
    */
  private [StitchHelpers] def accumulatorDNASequenceForLeftOverlap(reference: DNASequence,
                                                                   candidate: DNASequence,
                                                                   overlapLength: Int) = {
    val newSequence = candidate.sequence.dropRight(overlapLength) + reference.sequence
    val newFragment = candidate.fragmentName + "|" + reference.fragmentName
    DNASequence(newFragment, newSequence)
  }

  /**
    * This combines a candidate DNA sequence that crossed the set threshold for right overlap
    * with the reference DNA sequence.
    * @param reference - DNA sequence to which the candidate DNA sequence is prepended
    * @param candidate - gets appended to reference DNA sequence
    * @param overlapLength - length of text that overlaps between the candidate and reference
    * @return - Combined DNA sequence from candidate and reference.
    *         Both Fragment name and DNA sequence of candidate are appended to that of reference
    */
  private [StitchHelpers] def accumulatorDNASequenceForRightOverlap(reference: DNASequence,
                                                                    candidate: DNASequence,
                                                                    overlapLength: Int) = {
    val newSequence = reference.sequence + candidate.sequence.drop(overlapLength)
    val newFragment = reference.fragmentName + "|" +candidate.fragmentName
    DNASequence(newFragment, newSequence)
  }

  /**
    *
    * @param remainingDnaSequences - candidate DNA sequences
    * @param accumulator - reference DNA sequence
    * @return - list of overlapping texts for the beginning portion of the accumulator and
      ending portion of all dna sequences in remainingDnaSequences
    */
  private [StitchHelpers] def findLeftOverlap(remainingDnaSequences: List[DNASequence],
                                              accumulator: DNASequence) = {
    remainingDnaSequences.map{r =>
      r.sequence.tails.filter(_.length>0).find(accumulator.sequence.startsWith)
    }.filterNot(_.isEmpty)
  }

  /**
    *
    * @param remainingDnaSequences - candidate DNA sequences
    * @param accumulator - reference DNA sequence
    * @return - list of overlapping texts for the ending portion of the accumulator and
      beginning portion of all dna sequences in remainingDnaSequences
    */
  private [StitchHelpers] def findRightOverlap(remainingDnaSequences: List[DNASequence],
                                               accumulator: DNASequence) = {
    remainingDnaSequences.map{r =>
      accumulator.sequence.tails.filter(_.length>0).find(r.sequence.startsWith)
    }.filterNot(_.isEmpty)
  }


  private [StitchHelpers] def doesDNASequenceContainLeftOverlapTxt(dnaSequence: String, overlapText: String):Boolean =
    dnaSequence.endsWith(overlapText)

  private [StitchHelpers] def doesDNASequenceContainRightOverlapTxt(dnaSequence: String, overlapText: String):Boolean =
    dnaSequence.startsWith(overlapText)


  /**
    *
    * @param reference - This is the reference DNA sequence for which an overlap needs to be found
    * @param others - all other DNA sequences apart from the reference
    * @param findOverlap - function that finds a left or tight overlap
    * @param doesDNASequenceContainOverlapTxt
    * @param getDnaSequenceForAccumulator - combines the candidate and reference DNA sequence into one
    * @return - right or left overlapped combined DNA Sequence de
    */
  private [StitchHelpers] def getOverlap(reference: DNASequence,
                                         others: List[DNASequence],
                                         findOverlap: (List[DNASequence], DNASequence) => List[Option[String]],
                                         doesDNASequenceContainOverlapTxt: (String, String) => Boolean,
                                         getDnaSequenceForAccumulator: (DNASequence, DNASequence, Int)=> DNASequence) = {

    def getOverlapInternal(rem: List[DNASequence],
                           accumulator: DNASequence = DNASequence("","")): DNASequence = rem match {
      case Nil => accumulator
      case _ =>

        //finds max overlap text of accumulator and remaining
        val overlapTextOpt = findOverlap(rem, accumulator).
          maxBy(x => x.map(_.length)) //Assumption: that there should be only one maximum matching sequence

        val candidateOpt = overlapTextOpt.flatMap{ot=>
          rem.find(c=> doesDNASequenceContainOverlapTxt(c.sequence, ot))}

        if (!isCandidateQualified(overlapTextOpt, candidateOpt))
          getOverlapInternal(Nil, accumulator)
        else {
          val candidate = candidateOpt.get
          val overlapText = overlapTextOpt.get
          val newAccumulator = getDnaSequenceForAccumulator(accumulator, candidate, overlapText.length)
          val filteredRem = rem.filterNot(_ == candidate)
          getOverlapInternal(filteredRem, newAccumulator )
        }
    }
    getOverlapInternal(others, reference)
  }

  /**
    *
    * @param dnaSequences - structured DNA sequences that need to be combined into a chromosome
    * @return - Built chromosome or broken DNA sequence built from the DNA sequences
    */
  def constructChromosome(dnaSequences:List[DNASequence]):Either[DNASequence, Chromosome] = dnaSequences match {
    case Nil => Left(DNASequence("",""))
    case h::t =>
      val rightStitch = getOverlap(h,t,
        findRightOverlap,
        doesDNASequenceContainRightOverlapTxt,
        accumulatorDNASequenceForRightOverlap)

      val fragmentsInRightStitch = rightStitch.fragmentName.split('|')
      val availableFragments = dnaSequences.filterNot(x=> fragmentsInRightStitch.contains(x.fragmentName))

      val stitchedDNASequence = getOverlap(rightStitch,
        availableFragments,
        findLeftOverlap,
        doesDNASequenceContainLeftOverlapTxt,
        accumulatorDNASequenceForLeftOverlap)

      val fragmentsInChromosome = stitchedDNASequence.fragmentName.split('|')
      val unusedFragments = availableFragments.filterNot(x=> fragmentsInChromosome.contains(x.fragmentName))

      if (unusedFragments.nonEmpty){
        println(
          s"""Number of unused fragments = ${unusedFragments.length}
              |Unused fragment names = $unusedFragments
           """.stripMargin)
        Left(stitchedDNASequence)
      } else Right(Chromosome(stitchedDNASequence.fragmentName, stitchedDNASequence.sequence))
  }
}
