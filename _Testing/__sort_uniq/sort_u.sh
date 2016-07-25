date1=$(date +"%s")
sort -u -o ./ot1.out ./a1.in
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
sort -u -o ./ot2.out ./a2.in
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
cat ot1.out ot2.out >> ot.out
sort -u -o dif.out ot.out 
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
wc -l ./a1.in
wc -l ./a2.in
wc -l ./ot.out
wc -l ./dif.out
