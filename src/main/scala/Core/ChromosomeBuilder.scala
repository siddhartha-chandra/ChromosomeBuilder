package Core

import StitchHelpers.ChromosomeStitchHelpers.constructChromosome

import scala.io.Source

object ChromosomeBuilder{

  type Threshold = Double

  //denotes the ratio of overlapping text to the reference text.
  //Assigned as 0.5 as the problem mentions that overlap must be greater than half the length.
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
      ~~ Reconstructed: $category Chromosome ~~

      Fragments:
      $fragmentName

      Sequence:
      $sequence
      """)
  }
}