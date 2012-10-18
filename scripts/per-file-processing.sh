FILENAME=$1
#echo "Processing $FILENAME..."

# Restoring escaped characters (:#!=)
sed -i -e 's|\\\([:#!=]\)|\1|g' $FILENAME

# Convert unicode characters to native
#native2ascii -encoding UTF8 -reverse $FILENAME $FILENAME

# Remove standalone="no" in xml files
sed -i -e 's|" standalone="no"?>|"?>|g' $FILENAME

#echo "Done!"