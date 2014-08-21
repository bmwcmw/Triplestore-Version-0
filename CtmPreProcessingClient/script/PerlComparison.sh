perl -ne 'print if ($seen{$_} .= @ARGV) =~/10$/' $1 $2 | wc -l
