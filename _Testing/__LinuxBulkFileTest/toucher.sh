mkdir files
for i in {1..25000}
do
	for j in {1..100}
	do
		echo line0${i}_${j} >> "files/File${i}.txt"
	done
done
echo DONE...
cd files
ls -l | wc -l
du -h
