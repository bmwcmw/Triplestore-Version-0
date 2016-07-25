use strict;
use warnings;
use File::Basename;

my $file = $ARGV[0]; #perl xxx xxx
my $outputPath = $ARGV[1] 
my $name = basename($file);
print "Processing ".$name;
open(my $info, $file) or die "Could not open $file: $!";
my $fileS = $file.".S";
my $fileO = $file."O";
open(my $fS, '>>', $fileS) or die "Could not open file '$fileS' $!";
open(my $fO, '>>', $fileO) or die "Could not open file '$fileO' $!";

while(my $line = <$info>)  {
    my @fields = split(" " , $line);
    print $fS $fields[0]."\n";
    print $fO $fields[1]."\n";
}

close $info;
close fS;
close fO;