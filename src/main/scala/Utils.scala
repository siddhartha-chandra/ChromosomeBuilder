import Main._

import scala.annotation.tailrec

object Utils{

  def isCandidateQualified(overlapText: String, text: String) = {
    overlapText.length.toDouble/text.length > threshold
  }
  def isCandidateQualified(overlapTextOpt: Option[String], candidateOpt: Option[DNASequence]):Boolean = {
    (overlapTextOpt, candidateOpt) match {
      case (Some(overlapText), Some(dNASequence)) =>
        isCandidateQualified(overlapText, dNASequence.sequence)
      case _ => false
    }
  }

  def getDNASequences(dnaDataRaw: Iterator[String]) = {
    @tailrec
    def getDNASequencesInternal(remaining: List[String],
                                acc: List[DNASequence] = Nil): List[DNASequence] = remaining match {
      case Nil => acc
      case h::t if h.startsWith(">") =>
        val fragmentName = h.tail
        val (sequence, rem) = t.span(!_.startsWith(">"))
        getDNASequencesInternal(rem, acc :+ DNASequence(fragmentName, sequence.mkString))
    }
    getDNASequencesInternal(dnaDataRaw.toList)
  }

}

