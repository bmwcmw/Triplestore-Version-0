date1=$(date +"%s")
./GnuComparison.sh ./ot1.out ./ot2.out
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
./PerlComparison.sh ./ot1.out ./ot2.out
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
