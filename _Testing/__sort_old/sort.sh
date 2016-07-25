date1=$(date +"%s")
sort $1 -o $2
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
sort $3 -o $4
date2=$(date +"%s")
diff=$(($date2-$date1))
echo "$(($diff / 60)) minutes and $(($diff % 60)) seconds elapsed."
date1=$(date +"%s")
