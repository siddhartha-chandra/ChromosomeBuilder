import Utils._

import scala.annotation.tailrec

object ChromosomeStitchHelpers{

  /**
    *
    * @param reference - This is the reference DNA sequence for which a right overlap needs to be found
    * @param others - all other DNA sequences apart from the reference
    * @return - right overlapped combined DNA Sequence
    */

  private def getRightOverlap(reference: DNASequence, others: List[DNASequence]) = {
    def getNewAccumulator(reference: DNASequence, candidate: DNASequence, overlapLength: Int) = {
      val newSequence = reference.sequence + candidate.sequence.drop(overlapLength)
      val newFragment = reference.fragmentName + "|" +candidate.fragmentName
      DNASequence(newFragment, newSequence)
    }

    def getRightOverlapInternal(rem: List[DNASequence],
                                accumulator: DNASequence = DNASequence("","")): DNASequence = rem match {
      case Nil => accumulator
      case _ =>
        //finds max right overlap text of accumulator and others
        val overlapTextOpt = rem.map{o =>
          accumulator.sequence.tails.find(o.sequence.startsWith)
        }.maxBy(x => x.map(_.length)) //Assumption: that there should be only one maximum matching sequence

        val candidateOpt = overlapTextOpt.flatMap(ot=> others.find(_.sequence.startsWith(ot)))

        if (!isCandidateQualified(overlapTextOpt, candidateOpt))
          getRightOverlapInternal(Nil, accumulator)
        else {
          val candidate = candidateOpt.get
          val overlapText = overlapTextOpt.get
          val newAccumulator = getNewAccumulator(accumulator, candidate, overlapText.length)
          val filteredRem = rem.filterNot(_ == candidate)
          getRightOverlapInternal(filteredRem, newAccumulator )
        }
    }
    getRightOverlapInternal(others, reference)
  }


  /**
    *
    * @param reference - This is the reference DNA sequence for which a left overlap needs to be found
    * @param others - all other DNA sequences apart from the reference
    * @return - right overlapped combined DNA Sequence
    */

  //todo: there is redundancy between the rightOverlap and leftOverlap methods. Would like to remove that using HOF
  private def getLeftOverlap(reference: DNASequence, others: List[DNASequence]) = {
    //todo: place 3 for removing redundancy
    def getNewAccumulator(reference: DNASequence, candidate: DNASequence, overlapLength: Int) = {
      val newSequence = candidate.sequence.dropRight(overlapLength) + reference.sequence
      val newFragment = candidate.fragmentName + "|" + reference.fragmentName
      DNASequence(newFragment, newSequence)
    }

    def getLeftOverlapInternal(rem: List[DNASequence],
                                accumulator: DNASequence = DNASequence("","")): DNASequence = rem match {
      case Nil => accumulator
      case _ =>
        //finds max left overlap text of accumulator and others

        //todo: place 1 where redundancy can be removed
        val overlapTextOpt = rem.map{o =>
          o.sequence.tails.find(accumulator.sequence.startsWith)
        }.maxBy(x => x.map(_.length)) //Assumption: that there should be only one maximum matching sequence

        //todo: place 2 where redundancy can be removed
        val candidateOpt = overlapTextOpt.flatMap(ot=> others.find(_.sequence.endsWith(ot)))

        if (!isCandidateQualified(overlapTextOpt, candidateOpt))
          getLeftOverlapInternal(Nil, accumulator)
        else {
          val candidate = candidateOpt.get
          val overlapText = overlapTextOpt.get
          val newAccumulator = getNewAccumulator(accumulator, candidate, overlapText.length)
          val filteredRem = rem.filterNot(_ == candidate)
          getLeftOverlapInternal(filteredRem, newAccumulator )
        }
    }
    getLeftOverlapInternal(others, reference)
  }

  /**
    *
    * @param dnaSequences - structured DNA sequences that need to be combined into a chromosome
    * @return - combined DNA sequence
    */
  def constructChromosome(dnaSequences:List[DNASequence]) = dnaSequences match {
    case Nil => DNASequence("","")
    case h::t =>
      val rightStitch = getRightOverlap(h,t)
      val fragmentsInRightStitch = rightStitch.fragmentName.split('|')
      val availableFragments = dnaSequences.filterNot(x=> fragmentsInRightStitch.contains(x.fragmentName))
      val builtChromosome = getLeftOverlap(rightStitch,availableFragments)
      val fragmentsInChromosome = builtChromosome.fragmentName.split('|')
      val unusedFragments = availableFragments.filterNot(x=> fragmentsInChromosome.contains(x.fragmentName))

      if (unusedFragments.nonEmpty)
        println(
          s"""Number of unused fragments = ${unusedFragments.length}
             |Unused fragment names = $unusedFragments
           """.stripMargin)

      builtChromosome

  }
}