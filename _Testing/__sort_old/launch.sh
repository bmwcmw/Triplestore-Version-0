wc -l ./a1.in
wc -l ./a2.in
./sort.sh ./a1.in ./ot1.out ./a2.in ./ot2.out
ls -al
wc -l ./ot1.out
wc -l ./ot2.out
rm ./ot1.out
rm ./ot2.out
#ZAZA
wc -l ./a1.in
wc -l ./a2.in
./sort_n.sh ./a1.in ./ot1.out ./a2.in ./ot2.out 
ls -al
wc -l ./ot1.out
wc -l ./ot2.out
rm ./ot1.out
rm ./ot2.out
