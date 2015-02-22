#!/bin/bash
FILES=./_ps/*

for f in $FILES
do
  echo $f;
  
  date1=$(date +"%s")
  sort $f -o $f.sortedSO
  date2=$(date +"%s")
  diff=$(($date2-$date1))
  echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."

  date1=$(date +"%s")
  sort -k2 $f -o $f.sortedOS
  date2=$(date +"%s")
  diff=$(($date2-$date1))
  echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
  
  rm $f
done

#
#./a
#6 minutes and 32 seconds elapsed.
#7 minutes and 54 seconds elapsed.
#./-advisor
#0 minutes and 51 seconds elapsed.
#1 minutes and 7 seconds elapsed.
#./-doctoralDegreeFrom
#0 minutes and 8 seconds elapsed.
#0 minutes and 8 seconds elapsed.
#./-emailAddress
#2 minutes and 46 seconds elapsed.
#3 minutes and 38 seconds elapsed.
#./-headOf
#0 minutes and 1 seconds elapsed.
#0 minutes and 0 seconds elapsed.
#./-mastersDegreeFrom
#0 minutes and 7 seconds elapsed.
#0 minutes and 8 seconds elapsed.
#./-memberOf
#2 minutes and 37 seconds elapsed.
#3 minutes and 22 seconds elapsed.
#./-name
#5 minutes and 13 seconds elapsed.
#5 minutes and 57 seconds elapsed.
#./-publicationAuthor
#3 minutes and 33 seconds elapsed.
#5 minutes and 16 seconds elapsed.
#./-researchInterest
#0 minutes and 6 seconds elapsed.
#0 minutes and 7 seconds elapsed.
#./sort.sh
#0 minutes and 0 seconds elapsed.
#0 minutes and 0 seconds elapsed.
#./-subOrganizationOf
#0 minutes and 3 seconds elapsed.
#0 minutes and 4 seconds elapsed.
#./-takesCourse
#7 minutes and 57 seconds elapsed.
#10 minutes and 36 seconds elapsed.
#./-teacherOf
#0 minutes and 25 seconds elapsed.
#0 minutes and 31 seconds elapsed.
#./-teachingAssistantOf
#0 minutes and 6 seconds elapsed.
#0 minutes and 8 seconds elapsed.
#./-telephone
#2 minutes and 43 seconds elapsed.
#3 minutes and 33 seconds elapsed.
#./-undergraduateDegreeFrom
#0 minutes and 39 seconds elapsed.
#0 minutes and 41 seconds elapsed.
#./-worksFor
#0 minutes and 7 seconds elapsed.
#0 minutes and 9 seconds elapsed.
