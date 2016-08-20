import Utils._

import scala.annotation.tailrec

case class DNASequence(fragmentName: String, sequence: String)

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
  private def accumulatorDNASequenceForLeftOverlap(reference: DNASequence,
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
  private def accumulatorDNASequenceForRightOverlap(reference: DNASequence,
                                    candidate: DNASequence,
                                    overlapLength: Int) = {
    val newSequence = reference.sequence + candidate.sequence.drop(overlapLength)
    val newFragment = reference.fragmentName + "|" +candidate.fragmentName
    DNASequence(newFragment, newSequence)
  }

  private def findLeftOverlap(remainingDnaSequences: List[DNASequence],
                      accumulator: DNASequence) = {
    remainingDnaSequences.map{r =>
      r.sequence.tails.find(accumulator.sequence.startsWith)
    }
  }

  private def findRightOverlap(remainingDnaSequences: List[DNASequence],
                       accumulator: DNASequence) = {
    remainingDnaSequences.map{r =>
      accumulator.sequence.tails.find(r.sequence.startsWith)
    }
  }

  private def getMatchingTextForLeftOverLap(dnaSequence: String, overlapText: String):Boolean =
    dnaSequence.endsWith(overlapText)

  private def getMatchingTextForRightOverLap(dnaSequence: String, overlapText: String):Boolean =
    dnaSequence.startsWith(overlapText)

  /**
    *
    * @param reference - This is the reference DNA sequence for which a left overlap needs to be found
    * @param others - all other DNA sequences apart from the reference
    * @return - right overlapped combined DNA Sequence
    */

  private def getOverlap(reference: DNASequence,
                         others: List[DNASequence],
                         findOverlap: (List[DNASequence], DNASequence) => List[Option[String]],
                         matchingTextForOverlap: (String, String) => Boolean,
                         getDnaSequenceForAccumulator: (DNASequence, DNASequence, Int)=> DNASequence) = {

    def getOverlapInternal(rem: List[DNASequence],
                           accumulator: DNASequence = DNASequence("","")): DNASequence = rem match {
      case Nil => accumulator
      case _ =>

        //finds max overlap text of accumulator and remaining
        val overlapTextOpt = findOverlap(rem, accumulator).
          maxBy(x => x.map(_.length)) //Assumption: that there should be only one maximum matching sequence

        val candidateOpt = overlapTextOpt.flatMap{ot=>
          rem.find(c=> matchingTextForOverlap(c.sequence, ot))}

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
  def constructChromosome(dnaSequences:List[DNASequence]) = dnaSequences match {
    case Nil => DNASequence("","")
    case h::t =>
      val rightStitch = getOverlap(h,t,
        findRightOverlap,
        getMatchingTextForRightOverLap,
        accumulatorDNASequenceForRightOverlap)

      val fragmentsInRightStitch = rightStitch.fragmentName.split('|')
      val availableFragments = dnaSequences.filterNot(x=> fragmentsInRightStitch.contains(x.fragmentName))

      val stitchedDNASequence = getOverlap(rightStitch,
        availableFragments,
        findLeftOverlap,
        getMatchingTextForLeftOverLap,
        accumulatorDNASequenceForLeftOverlap)

      val fragmentsInChromosome = stitchedDNASequence.fragmentName.split('|')
      val unusedFragments = availableFragments.filterNot(x=> fragmentsInChromosome.contains(x.fragmentName))

      if (unusedFragments.nonEmpty){
        println(  
          s"""Number of unused fragments = ${unusedFragments.length}
              |Unused fragment names = $unusedFragments
           """.stripMargin)
      }
      stitchedDNASequence
  }
}