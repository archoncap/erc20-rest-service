pragma solidity ^0.4.0;

contract InfoFeed {
    uint256 public testWrite = 1000;
    function contractCall() public returns (uint256 ret){
        testWrite += 100;
        return 100;
    }
}

contract Consumer {
    InfoFeed feed;
    function setFeed(address addr) public { feed = InfoFeed(addr); }
    function callContract() public{
        uint256 callResultValue = feed.contractCall.gas(1000000)();
        callResult(callResultValue);
    }

    event callResult(uint256 _value);
}