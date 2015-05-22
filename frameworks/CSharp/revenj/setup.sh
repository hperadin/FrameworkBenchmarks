#!/bin/bash

. ${IROOT}/mono.installed

echo "Cleaning up..."
rm -rf $TROOT/exe $TROOT/packages $TROOT/tmp

echo "Setting up the directories"
mkdir -p $TROOT/exe $TROOT/tmp

echo "Trying to restore dependencies via NuGet..."
mono NuGet.exe restore 

echo "Compiling the server model, and downloading dependencies..."
java -jar $TROOT/lib/dsl-clc.jar \
    -temp=$TROOT/tmp/ \
    -dsl=$TROOT/Revenj.Bench \
    -settings=manual-json,no-jackson \
    -namespace=framework.benchmarks \
    -revenj=$TROOT/exe/ServerModel.dll \
    -no-prompt \
    -compiler \
    -dependencies=$TROOT/exe \
    -download \
    -migration \
    -apply \
    -db="${DBHOST}:5432/hello_world?user=benchmarkdbuser&password=benchmarkdbpass"

echo "Cleaning up dependencies"
mv $TROOT/exe/revenj/* exe/
rmdir $TROOT/exe/revenj

echo "Compiling the benchmark project..."
xbuild $TROOT/Revenj.Bench/Revenj.Bench.csproj

echo "Copying the configuration template"
cat $TROOT/Revenj.Http.exe.config.template | sed 's|\(ConnectionString.*server=\)localhost|\1'"${DBHOST}"'|' > $TROOT/exe/Revenj.Http.exe.config

echo "Running the Revenj instance"
mono $TROOT/exe/Revenj.Http.exe
sleep 5
