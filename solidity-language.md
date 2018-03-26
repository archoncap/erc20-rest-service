# solidity-language 类型系统,单位和全局变量

本文档主要说明solidity一些比较复杂的语法

### require
用例如下:
```
// Give `voter` the right to vote on this ballot.
    // May only be called by `chairperson`.
    function giveRightToVote(address voter) public {
        // If the argument of `require` evaluates to `false`,
        // it terminates and reverts all changes to
        // the state and to Ether balances. It is often
        // a good idea to use this if functions are
        // called incorrectly. But watch out, this
        // will currently also consume all provided gas
        // (this is planned to change in the future).
        require(
            (msg.sender == chairperson) &&
            !voters[voter].voted &&
            (voters[voter].weight == 0)
        );
        voters[voter].weight = 1;
    }

```

说明:'requre' 返回参数为'false',那么它将会撤销所有`state`的改变.当函数调用不正确的时候采用`require`进行校验非常不错.

但是需要注意的是,使用`require`将花费掉所有提供的gas,在后续的版本中将对该特性进行修改.

### modifier
`modifier`用例如下:

```
/// Modifiers are a convenient way to validate inputs to
    /// functions. `onlyBefore` is applied to `bid` below:
    /// The new function body is the modifier's body where
    /// `_` is replaced by the old function body.
    modifier onlyBefore(uint _time) { require(now < _time); _; }
    modifier onlyAfter(uint _time) { require(now > _time); _; }
    
    /// Place a blinded bid with `_blindedBid` = keccak256(value,
    /// fake, secret).
    /// The sent ether is only refunded if the bid is correctly
    /// revealed in the revealing phase. The bid is valid if the
    /// ether sent together with the bid is at least "value" and
    /// "fake" is not true. Setting "fake" to true and sending
    /// not the exact amount are ways to hide the real bid but
    /// still make the required deposit. The same address can
    /// place multiple bids.
    function bid(bytes32 _blindedBid)
        public
        payable
        onlyBefore(biddingEnd)
    {
        bids[msg.sender].push(Bid({
            blindedBid: _blindedBid,
            deposit: msg.value
        }));
    }

    /// Reveal your blinded bids. You will get a refund for all
    /// correctly blinded invalid bids and for all bids except for
    /// the totally highest.
    function reveal(
        uint[] _values,
        bool[] _fake,
        bytes32[] _secret
    )
        public
        onlyAfter(biddingEnd)
        onlyBefore(revealEnd)
    {
        uint length = bids[msg.sender].length;
        require(_values.length == length);
        require(_fake.length == length);
        require(_secret.length == length);

        uint refund;
        for (uint i = 0; i < length; i++) {
            var bid = bids[msg.sender][i];
            var (value, fake, secret) =
                    (_values[i], _fake[i], _secret[i]);
            if (bid.blindedBid != keccak256(value, fake, secret)) {
                // Bid was not actually revealed.
                // Do not refund deposit.
                continue;
            }
            refund += bid.deposit;
            if (!fake && bid.deposit >= value) {
                if (placeBid(msg.sender, value))
                    refund -= value;
            }
            // Make it impossible for the sender to re-claim
            // the same deposit.
            bid.blindedBid = bytes32(0);
        }
        msg.sender.transfer(refund);
    }
```
`modifier`是一种用来校验函数输出的非常方便的方法.

在bid函数使用了onBefore函数校验输出,在reveal函数中使用了onAfter函数校验输出.需要说明的是,onBefore和onAfter与function,public等关键词位于同一行.

bid的写法: function bid(bytes32 _blindedBid) public payable onlyBefore(biddingEnd)

reveal的写法: function reveal(uint[] _values,bool[] _fake,bytes32[] _secret)public onlyAfter(biddingEnd) onlyBefore(revealEnd)

可以看出,modifier支持多重嵌套.当使用modifier时,实际执行的函数体是将旧函数体替换modifier中"_"字符,得到的新函数体.所以`bid`实际执行的函数是:
```
{
        require(now > _time);
        bids[msg.sender].push(Bid({
            blindedBid: _blindedBid,
            deposit: msg.value
        }));
    }

```
### internal
internal使用例子:
```
 function placeBid(address bidder, uint value) internal
            returns (bool success)
```
internal标识符的主要含义是该函数只能在contract中内部调用.

### payable

payable函数非常关键.payable两个使用用例:
```
function deposit() payable {
  deposits[msg.sender] += msg.value;
}; 
```
```
function bid() public payable {
        // No arguments are necessary, all
        // information is already part of
        // the transaction. The keyword payable
        // is required for the function to
        // be able to receive Ether.

        // Revert the call if the bidding
        // period is over.
        require(now <= auctionEnd);

        // If the bid is not higher, send the
        // money back.
        require(msg.value > highestBid);

        if (highestBid != 0) {
            // Sending back the money by simply using
            // highestBidder.send(highestBid) is a security risk
            // because it could execute an untrusted contract.
            // It is always safer to let the recipients
            // withdraw their money themselves.
            pendingReturns[highestBidder] += highestBid;
        }
        highestBidder = msg.sender;
        highestBid = msg.value;
        emit HighestBidIncreased(msg.sender, msg.value);
    }
```
从上面两个例子可以看出,两个例子都没有接受输入参数,但是在函数体中均使用了`msg.value`字段.

The keyword payable is required for the function to be able to receive Ether.

Payable allows a function to receive ether while being called as stated in docs.It's manadatory from solidity 0.4.x. If you try to send ether using call:

token.foo.call.value("ETH_TO_BE_SENT")("ADDITIONAL_DATA")

to a function without a payable modifier, the transaction will be rejected.

说明如下:当函数接受ether时,那么必须表明payable标识符.

从0.4.x中引入了Payable关键字符,如果使用如下的call语句
```
token.foo.call.value("ETH_TO_BE_SENT")("ADDITIONAL_DATA")
```
如果函数没有Payable关键符,则会被拒绝.

### Function Types
Solidity 函数格式如下:
```
function (<parameter types>) {internal|external} [pure|constant|view|payable] [returns (<return types>)]
```

internal之前已经提到过,只能从内部调用.

External函数可以供外部调用,默认未标注情形下,所有的函数都是internal类型.

关于 pure constant view payable的讨论:

constant indicates that network verification won't be necessary. Callers receive return values (quickly, from local storage and processing) instead of transaction hashes.

Start with solc 0.4.17, constant is depricated in favor of two new and more specific modifiers.

View This is generally the replacement for constant. It indicates that the function will not alter the storage state in any way.

Pure This is even more restrictive, indicating that it won't even read the storage state.

A pure function might look something like this very contrived example:
```
function returnTrue() public pure returns(bool response) {
    return true;
}

```

说明如下:
- constant从0.4.17开始废弃,constant的字段的引入是为了提高效率,表明函数的执行不需要network verfication.
- view 表明这个函数的执行不会改变storage的状态.
- Pure 具有更强的限制意义,表明这个函数的执行甚至不会读取storage的状态.

最后在上面罗列了一个Pure函数的使用.

### 类型系统
在solidity语言中提供了单位,使用如下:

```
function f(uint start, uint daysAfter) public {
    if (now >= start + daysAfter * 1 days) {
      // ...
    }
}
```
从上面的例子中看出,可以用来1 days和now全局变量进行比较.
```
1 == 1 seconds
1 minutes == 60 seconds
1 hours == 60 minutes
1 days == 24 hours
1 weeks == 7 days
1 years == 365 days
```

在fabric中,提供了一些通用的全局变量和全局方法,供智能合约调用,如下:

```
block.blockhash(uint blockNumber) returns (bytes32): hash of the given block - only works for 256 most recent blocks excluding current
block.coinbase (address): current block miner’s address
block.difficulty (uint): current block difficulty
block.gaslimit (uint): current block gaslimit
block.number (uint): current block number
block.timestamp (uint): current block timestamp as seconds since unix epoch
gasleft() returns (uint256): remaining gas
msg.data (bytes): complete calldata
msg.gas (uint): remaining gas - deprecated in version 0.4.21 and to be replaced by gasleft()
msg.sender (address): sender of the message (current call)
msg.sig (bytes4): first four bytes of the calldata (i.e. function identifier)
msg.value (uint): number of wei sent with the message
now (uint): current block timestamp (alias for block.timestamp)
tx.gasprice (uint): gas price of the transaction
tx.origin (address): sender of the transaction (full call chain)
```
一个now函数的使用:
```
function f(uint start, uint daysAfter) public {
    if (now >= start + daysAfter * 1 days) {
      // ...
    }
}
```

使用例子:
```
Do not rely on block.timestamp, now and block.blockhash as a source of randomness, unless you know what you are doing.
```


### Visibility and Getters

关于Visibility和Getter的英文官方文档:

http://solidity.readthedocs.io/en/v0.4.21/contracts.html#visibility-and-getters

对于function而言,有四种Visibility(可见性)变量,对于state variable而言,有三种Visibility(可见性)变量.
##### Visibility

```
Since Solidity knows two kinds of function calls (internal ones that do not create an actual EVM call (also called a “message call”) and external ones that do), there are four types of visibilities for functions and state variables.

Functions can be specified as being external, public, internal or private, where the default is public. For state variables, external is not possible and the default is internal.

external:
External functions are part of the contract interface, which means they can be called from other contracts and via transactions. An external function f cannot be called internally . External functions are sometimes more efficient when they receive large arrays of data.
public:
Public functions are part of the contract interface and can be either called internally or via messages. For public state variables, an automatic getter function (see below) is generated.
internal:
Those functions and state variables can only be accessed internally (i.e. from within the current contract or contracts deriving from it), without using this.
private:
Private functions and state variables are only visible for the contract they are defined in and not in derived contracts.
```
翻译:
solidity 中存在这两种函数调用(internal类型不创建确切的EVM调用,同样被成为"message call"消息调用),external创建确切的EVM调用.

在Ethereum中,针对函数存在着四种Visibility(可见性)限定,针对state变量存在这三种Visibility(可见性)限定.

Functions可以指定为external,public,internal和private,默认public.对于state variable,external不支持,默认为internal.

external:
external 函数是一部分智能合约的接口,表明他们可以被其他的智能合约和外部的交易调用.一个external函数f不能够从内部调用.当接受大量数据数组时,外部函数更高效一些.
public:public只能被内部调用也可以通过消息调用.对于public state变量,会自动生成getter函数.
internal:内部函数和变量智能内部访问(当前智能合约以及继承当前智能合约的智能合约)
private: private只能被内部调用(当前智能合约可行,继承当前智能合约的应用不可行)

##### Getter

The compiler automatically creates getter functions for all public state variables. For the contract given below, the compiler will generate a function called data that does not take any arguments and returns a uint,
```
pragma solidity ^0.4.0;

contract C {
    uint public data = 42;
}

contract Caller {
    C c = new C();
    function f() public {
        uint local = c.data();
    }
}
```
The getter functions have external visibility. 

```
pragma solidity ^0.4.0;

contract Complex {
    struct Data {
        uint a;
        bytes3 b;
        mapping (uint => uint) map;
    }
    mapping (uint => mapping(bool => Data[])) public data;
}
```
The Auto-generated code is below:
```
function data(uint arg1, bool arg2, uint arg3) public returns (uint a, bytes3 b) {
    a = data[arg1][arg2][arg3].a;
    b = data[arg1][arg2][arg3].b;
}

```

编译器自动为所有的public state variable生成getter函数,例子如上.

getter函数具有external属性.
