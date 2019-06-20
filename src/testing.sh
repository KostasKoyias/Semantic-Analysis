#!/bin/bash

# get the right file paths under parent directory
main="Main"
pass=$(find .. -name "pass")
fail=$(find .. -name "fail")

# check whether the offsets flag is on
offsets=""
if [ "$1" == "--offsets" ]
then
    offsets=$1
fi

# for each test case folder do
for folder in {$pass,$fail}
do

    # display an informative message to the user about what follows
    if [ $folder == $pass ]
    then
        echo "The following should pass the Semantic Test"
    else
        echo "The following should fail the Semantic Test"
    fi
    
    # type check each file in the folder
    for i in $(ls $folder) 
    do
        java $main $folder/$i $offsets

        # allow user to take a look at the input file using the default editor
        ans=""
        while [ "$ans" != "y" ] && [ "$ans" != "n" ]
        do
            echo "want to see input file(y/n)"
            read -n 1 ans
        done
        if [ $ans == "y" ]
        then
            editor $folder/$i
        fi

        # allow user to skip the rest of the files in this directory
        ans=""
        while [ "$ans" != "y" ] && [ "$ans" != "n" ]
        do
            echo "continue(y/n)"
            read -n 1 ans
        done
        if [ $ans == "n" ]
        then
            break
        fi
    done
done
if [ "$offsets" == "" ]
then 
    echo -e "testing.sh: To view field and method offsets for each class rerun with \e[1m--offsets\e[0m" 
fi
exit 0