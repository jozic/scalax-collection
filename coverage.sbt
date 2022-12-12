import scoverage._

ScoverageKeys.coverageMinimumStmtTotal := 100
ScoverageKeys.coverageMinimumBranchTotal := 100
ScoverageKeys.coverageFailOnMinimum := true
ScoverageKeys.coverageHighlighting := true

//https://github.com/scoverage/sbt-scoverage/issues/84
addCommandAlias("scoverage", "clean;coverage;test;coverageReport;coverageOff")
