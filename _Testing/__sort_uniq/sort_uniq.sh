#8 minutes and 43 seconds elapsed.
#8 minutes and 40 seconds elapsed.
#14 minutes and 1 seconds elapsed.
date1=$(date +"%s")
sort ./a1.in | uniq > ot.out
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
sort ./a2.in | uniq >> ot.out
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
sort ot.out | uniq > dif.out
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
wc -l ./a1.in
wc -l ./a2.in
wc -l ./ot.out
wc -l ./dif.out
