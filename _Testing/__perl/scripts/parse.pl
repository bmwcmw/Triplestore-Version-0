#!/usr/bin/perl

if ($#ARGV != 4) {
	print "Usage: parse.pl ntriplesfile encoded-t-path common-s-o-file 1|0(1:write common s-o, 0:read common-so) 1|0(1:writing out mappings, 0:no-op)\n";
	exit -1;
}

my $count = 0;
my $file = $ARGV[0];
my %hashsubj=();
my %hashpred=();
my %hashobj=();
my $count_s = 1;
my $count_p = 1;
my $count_o = 1;
my $tmp;
my $outfile = $ARGV[1];
my $sofile = $ARGV[2];
my $option = $ARGV[3];
my $write_mapping = $ARGV[4];

print "$file\n";

##############
# Make sure to make changes at 4 places
#############

open FILE, $file or die "cannot open data file\n";
open TRIPLES, ">$outfile" or die "Cannot open encoded triples file $outfile\n";
$stats = $file."_total_count";
print "$stats\n";
open FILE3, ">$stats" or die "Cannot open stats file\n";
if ($option == 1) {
	open SOFILE, ">$sofile" or die "Cannot open $sofile\n";
} else {
	open SOFILE, $sofile or die "Cannot open $sofile\n";
}

$maxs = 0;
$maxp = 0;
$maxo = 0;

my $count_so = 0;
if ($option == 0) {
	$count_so = 0;
	while(<SOFILE>) {
		my $str = $_;
		chomp $str;
		$hashsubj{$str} = $count_s;
		$hashobj{$str} = $count_o;
		
		$count_s++;
		$count_o++;
		$count_so++;
	}
}

while(<FILE>) {
	chomp;
	#empty line, don't do anything
	if (/^[\s\t]*$/) {}
	else {
		#replace leading spaces and tabs
		s/^[\s\t]+//;
		/^(.+?)\s+(<.+?>)\s+(.+?)\s*\.$/ or do {warn "No match found for $_\n"; next};
#		print "Subj=$1 pred=$2 obj=$3\n";
		$subj = $1; $pred = $2; $obj = $3;		
		chomp $subj;
		chomp $pred;
		chomp $obj;
		my $spos=0, $ppos=0, $opos=0;
		if (!exists $hashsubj{$subj}) {
#			print "S NOT exists\n";
			$hashsubj{$subj} = $count_s;
			$spos = $count_s;
			$count_s++;
		} else { 
			$spos = $hashsubj{$subj}; 
		}
		if (!exists $hashpred{$pred}) {
#			print "P NOT exists\n";
			$hashpred{$pred} = $count_p;
			$ppos = $count_p;
			$count_p++;
		} else {
			$ppos = $hashpred{$pred};
		}
		if (!exists $hashobj{$obj}) {
#			print "O NOT exists\n";
			$hashobj{$obj} = $count_o;
			$opos = $count_o;
			$count_o++
		} else {
			$opos = $hashobj{$obj};
		}
#		print "Enter"; $tmp = <stdin>;
		if ($option == 0){
			print TRIPLES "$spos:$ppos:$opos\n";
		}
#		print "$spos|$ppos|$opos\n";
		if ($maxs < $spos) {
			$maxs = $spos;
		}
		if ($maxp < $ppos) {
			$maxp = $ppos;
		}
		if ($maxo < $opos) {
			$maxo = $opos;
		}
		$count++;
	}
}

#to find common s-o

#
if ($option == 1) {
	$count_so = 0;
	for my $s (keys %hashsubj) {
#		my $spos = $hashsubj{$s};
		if (exists $hashobj{$s}) {
			my $opos = $hashobj{$s};
#			print SOFILE "$count $spos $opos $s\n";
			print SOFILE "$s\n";
			$count_so++;
		}
	}
}

print FILE3 "Total triples $count\n Distinct subj $maxs\n Distinct pred $maxp\n Distinct obj $maxo\n Common SO $count_so\n";

close TRIPLES;
close SOFILE;
close FILE;

if ($write_mapping == 1) {
	open SMAPPING, ">$outfile"."_revsubj" or die "could not open $outfile"."_revsubj\n";
	open PMAPPING, ">$outfile"."_revpred" or die "could not open $outfile"."_revpred\n";
	open OMAPPING, ">$outfile"."_revobj" or die "could not open $outfile"."_revobj\n";
	open S_STR_INT, ">$outfile"."_subj" or die "could not open $outfile"."_subj\n";
	open P_STR_INT, ">$outfile"."_pred" or die "could not open $outfile"."_pred\n";
	open O_STR_INT, ">$outfile"."_obj" or die "could not open $outfile"."_obj\n";

	%revsubjmap = reverse %hashsubj;
	%hashsubj = ();

	my $substrlen = 0;
	my $predstrlen = 0;
	my $objstrlen = 0;

	foreach $key(sort { $a <=> $b } keys %revsubjmap) {
		my $value = substr($revsubjmap{$key}, 0, 2047);

		$substrlen += length $value;

#		print SMAPPING "$key $revsubjmap{$key}\n";
#		print S_STR_INT "$revsubjmap{$key} $key\n";
		print SMAPPING "$key $value\n";
		print S_STR_INT "$value $key\n";
	}
	close SMAPPING;
	close S_STR_INT;
	
	%revpredmap = reverse %hashpred;
	%hashpred = ();
	foreach $key(sort { $a <=> $b } keys %revpredmap) {
		my $value = substr($revpredmap{$key}, 0, 2047);

		$predstrlen += length $value;

		print PMAPPING "$key $value\n";
		print P_STR_INT "$value $key\n";
	}
	close PMAPPING;
	close P_STR_INT;
	
	%revobjmap = reverse %hashobj;
	%hashobj = ();
	foreach $key(sort { $a <=> $b } keys %revobjmap) {
		my $value = substr($revobjmap{$key}, 0, 2047);

		$objstrlen += length $value;

		print OMAPPING "$key $value\n";
		print O_STR_INT "$value $key\n";
	}
	close OMAPPING;
	close O_STR_INT;

	$avgsubstr;
	$avgpredstr;
	$avgobjstr;

	$avgsubstr = $substrlen/$maxs;
	$avgpredstr = $predstrlen/$maxp;
	$avgobjstr = $objstrlen/$maxo;

	print FILE3 "Avg subject len $avgsubstr\n";
	print FILE3 "Avg predicate len $avgpredstr\n";
	print FILE3 "Avg object len $avgobjstr\n";

}
close FILE3;
#while(<FILE>) {
#	#empy line, don't do anything
#	if (/^[\s\t]*\n/) {}
#	else {
#		#remove leading spaces and tabs
#		s/^[\s\t]+//;
#		#ignore @prfix lines
#		if(/^\@prefix/) {
#			print FILE2 $_;
#		}
#		elsif (/(.+)[\s\t]+.+,/) {
#			@arr = split(/[\s,]/);
#			for($i = 1; $i <= $#arr; i++) {
#				print FILE2 "$subj $1 $arr[$i] .\n";
#			}
#		}
#		elsif (/(.+)[\s\t]+(.+)[\s\t]+(.+)[\s\t]*;\n/) {
#			$subj=$1; $pred=$2; $obj=$3; 	
#			print FILE2 "$subj $pred $obj .\n";
#			$count++;
#		} elsif (/(.+)[\s\t]+(.+)[\s\t]*;\n/) {
#			print FILE2 "$subj $1 $2 .\n";
#			$count++;
#		}
#	}
#}

