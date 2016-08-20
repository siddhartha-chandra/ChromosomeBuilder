package Core

import StitchHelpers.ChromosomeStitchHelpers.constructChromosome

import scala.io.Source


object Main{

  type Threshold = Double
  //as more than half the length for overlap is the criteria
  val threshold:Threshold = 0.5
  val currentDirectory =  new java.io.File(".").getCanonicalPath
  val fileName = "data_set.txt"
  val filePath = s"$currentDirectory/src/main/resources/$fileName"

  def main(args: Array[String]): Unit = {
    val lines = Source.fromFile(filePath).getLines()
    val DNASequences = Utils.getDNASequences(lines)
    val constructedChromosome = constructChromosome(DNASequences)


    val (category, fragmentName, sequence) = constructedChromosome match {
      case Left(brokenChromosome) =>
        ("Broken", brokenChromosome.fragmentName, brokenChromosome.sequence)
      case Right(chromosome) =>
        ("Complete", chromosome.fragmentName, chromosome.sequence)
    }

    println(
      s"""
      ~~Reconstructed: $category Chromosome~~

      Fragments:
      $fragmentName

      Sequence:
      $sequence
      """)

  }
}