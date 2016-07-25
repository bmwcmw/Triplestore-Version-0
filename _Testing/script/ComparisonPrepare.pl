use strict;
use warnings;
use File::Basename;
use File::Spec;

my $filePath = $ARGV[0]; #perl xxx xxx
my $outputPath = $ARGV[1];
my $fileName = basename($filePath);
print "Processing ".$filePath;
open(my $info, $filePath) or die "Could not open $filePath: $!";
my $fileS = File::Spec->catfile( $outputPath, $fileName.".S" );
my $fileO = File::Spec->catfile( $outputPath, $fileName.".O" );
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