#!/usr/bin/env bash

cd src/main/resources/solidity/contract/ && \
    solc --bin --abi --optimize --overwrite DoubleToken.sol -o build/ && \
    web3j solidity generate \
        build/Consumer.bin \
        build/Consumer.abi \
        -p io.blk.erc20.generated \
        -o ../../../java/ && \
    web3j solidity generate \
        build/InfoFeed.bin \
        build/InfoFeed.abi \
        -p io.blk.erc20.generated \
        -o ../../../java/ && \
    solc --bin --abi --optimize --overwrite OwnedTokenCreator.sol -o build/ && \
    web3j solidity generate \
        build/OwnedToken.bin \
        build/OwnedToken.abi \
        -p io.blk.erc20.generated \
        -o ../../../java/ && \
    web3j solidity generate \
        build/TokenCreator.bin \
        build/TokenCreator.abi \
        -p io.blk.erc20.generated \
        -o ../../../java/
