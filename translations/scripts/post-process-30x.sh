#!/bin/bash
#
# Copyright (C) 2003-2012 eXo Platform SAS.
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU Affero General Public License
# as published by the Free Software Foundation; either version 3
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, see<http://www.gnu.org/licenses/>.#
#
#
# Purpose : Restore projects structure
#


# eXoProjects directory 
EXO_PROJECTS=`pwd`

projects=( 'platform' 'ecms' 'cs' 'ks' 'social' 'exogtn' )
versions=( '3.0.x' '2.1.x' '2.1.x' '2.1.x' '1.1.x' '3.1.x' )
length=${#projects[@]}

echo "=========================Restoring projects structure========================="
echo ""

for (( i=0;i<$length;i++)); do
  if [ -d $EXO_PROJECTS/${projects[${i}]}-${versions[${i}]} ]; then
    mv ${projects[${i}]}-${versions[${i}]} ${projects[${i}]}
    echo "Renamed ${projects[${i}]}-${versions[${i}]} to ${projects[${i}]}"
  fi
done

echo ""
echo "=========================Finished=============================================="