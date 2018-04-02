# 以太坊公链应用难点

以太坊公链应用需要考虑以下几个问题:
- 双币的交互:双币怎样互相调用,通信?
- 分红gas的开销:对十万人的能量块持有者进行分红,需要写入的交易太多.
- 与交易所的对接:交易所是怎样对接各种不同的代币?
- 智能合约的升级:如何对智能合约进行数据和代码逻辑上的升级,需要考虑哪些问题?

### 双币的交互
下面是三个智能合约相互调用的例子:
```
pragma solidity ^0.4.0;

contract InfoFeed {
    function info() public payable returns (uint ret) { return 42; }
}

contract Consumer {
    InfoFeed feed;
    function setFeed(address addr) public { feed = InfoFeed(addr); }
    function callFeed() public { feed.info.value(10).gas(800)(); }
}
```

```

pragma solidity ^0.4.16;

interface token {
    function transfer(address receiver, uint amount);
}

contract Crowdsale {
    address public beneficiary;
    uint public fundingGoal;
    uint public amountRaised;
    uint public deadline;
    uint public price;
    token public tokenReward;
    mapping(address => uint256) public balanceOf;
    bool fundingGoalReached = false;
    bool crowdsaleClosed = false;

    event GoalReached(address recipient, uint totalAmountRaised);
    event FundTransfer(address backer, uint amount, bool isContribution);

    /**
     * Constrctor function
     *
     * Setup the owner
     */
    function Crowdsale(
        address ifSuccessfulSendTo,
        uint fundingGoalInEthers,
        uint durationInMinutes,
        uint etherCostOfEachToken,
        address addressOfTokenUsedAsReward
    ) {
        beneficiary = ifSuccessfulSendTo;
        fundingGoal = fundingGoalInEthers * 1 ether;
        deadline = now + durationInMinutes * 1 minutes;
        price = etherCostOfEachToken * 1 ether;
        tokenReward = token(addressOfTokenUsedAsReward);
    }

    /**
     * Fallback function
     *
     * The function without name is the default function that is called whenever anyone sends funds to a contract
     */
    function () payable {
        require(!crowdsaleClosed);
        uint amount = msg.value;
        balanceOf[msg.sender] += amount;
        amountRaised += amount;
        tokenReward.transfer(msg.sender, amount / price);
        FundTransfer(msg.sender, amount, true);
    }

    modifier afterDeadline() { if (now >= deadline) _; }

    /**
     * Check if goal was reached
     *
     * Checks if the goal or time limit has been reached and ends the campaign
     */
    function checkGoalReached() afterDeadline {
        if (amountRaised >= fundingGoal){
            fundingGoalReached = true;
            GoalReached(beneficiary, amountRaised);
        }
        crowdsaleClosed = true;
    }


    /**
     * Withdraw the funds
     *
     * Checks to see if goal or time limit has been reached, and if so, and the funding goal was reached,
     * sends the entire amount to the beneficiary. If goal was not reached, each contributor can withdraw
     * the amount they contributed.
     */
    function safeWithdrawal() afterDeadline {
        if (!fundingGoalReached) {
            uint amount = balanceOf[msg.sender];
            balanceOf[msg.sender] = 0;
            if (amount > 0) {
                if (msg.sender.send(amount)) {
                    FundTransfer(msg.sender, amount, false);
                } else {
                    balanceOf[msg.sender] = amount;
                }
            }
        }

        if (fundingGoalReached && beneficiary == msg.sender) {
            if (beneficiary.send(amountRaised)) {
                FundTransfer(beneficiary, amountRaised, false);
            } else {
                //If we fail to send the funds to beneficiary, unlock funders balance
                fundingGoalReached = false;
            }
        }
    }
}

```


```
pragma solidity ^0.4.16;

contract OwnedToken {
    // TokenCreator is a contract type that is defined below.
    // It is fine to reference it as long as it is not used
    // to create a new contract.
    TokenCreator creator;
    address owner;
    bytes32 name;

    // This is the constructor which registers the
    // creator and the assigned name.
    function OwnedToken(bytes32 _name) public {
        // State variables are accessed via their name
        // and not via e.g. this.owner. This also applies
        // to functions and especially in the constructors,
        // you can only call them like that ("internally"),
        // because the contract itself does not exist yet.
        owner = msg.sender;
        // We do an explicit type conversion from `address`
        // to `TokenCreator` and assume that the type of
        // the calling contract is TokenCreator, there is
        // no real way to check that.
        creator = TokenCreator(msg.sender);
        name = _name;
    }

    function changeName(bytes32 newName) public {
        // Only the creator can alter the name --
        // the comparison is possible since contracts
        // are implicitly convertible to addresses.
        if (msg.sender == address(creator))
            name = newName;
    }

    function transfer(address newOwner) public {
        // Only the current owner can transfer the token.
        if (msg.sender != owner) return;
        // We also want to ask the creator if the transfer
        // is fine. Note that this calls a function of the
        // contract defined below. If the call fails (e.g.
        // due to out-of-gas), the execution here stops
        // immediately.
        if (creator.isTokenTransferOK(owner, newOwner))
            owner = newOwner;
    }
}

contract TokenCreator {
    function createToken(bytes32 name)
       public
       returns (OwnedToken tokenAddress)
    {
        // Create a new Token contract and return its address.
        // From the JavaScript side, the return type is simply
        // `address`, as this is the closest type available in
        // the ABI.
        return new OwnedToken(name);
    }

    function changeName(OwnedToken tokenAddress, bytes32 name)  public {
        // Again, the external type of `tokenAddress` is
        // simply `address`.
        tokenAddress.changeName(name);
    }

    function isTokenTransferOK(address currentOwner, address newOwner)
        public
        view
        returns (bool ok)
    {
        // Check some arbitrary condition.
        address tokenAddress = msg.sender;
        return (keccak256(newOwner) & 0xff) == (bytes20(tokenAddress) & 0xff);
    }
}
```

上述三个代码均涉及到了智能合约在链上的互相调用,主要的方式是将智能合约部署后的地址传入到新的智能合约中,然后使用该智能合约中的函数.

需要补充说明的是,interface和contract均是智能合约修饰符,在contract声明的智能合约可以即写实现,又写接口,在interface中定义的只需要将接口写出.

此外,可以在其它智能合约中根据地址创建智能合约对象,在智能合约的升级中具有显著的意义.

**示例1已经测试**

### 分红gas的开销
当在以太坊链上进行大量的操作需要消耗大量的gas,尤其在本例子中的对数以万计的人进行分红.

为了解决gas消耗的问题,参考neo(小蚁股)的分红方案,初步构想了分红池和分布式提取的方式.

分红池数据结构如下:
```
map(number->amount) bonuspool
```
其中 number为第几个分红的周期,如分红周期为五天,则number的含义为第一个五天,第二个五天,第三个五天,...

amount为该分红周期的分红池内的收益,具体含义为第一个五天的全网待分红数额,第二个五天的全网待分红数额,...

在区块链上的读写不需要消耗gas,用户可以每次查询到它当前待分红的数额,但需要执行提交或转账操作才能讲这部分待分红的数值转入到自己的账户下.

用户每次查询账户的待分红数值时,会根据他所持有的block及全网的block进行计算.


### 与交易所的对接
交易所维护ERC20的接口,或取代币的地址,并不断查询行情.定价收手续费.



### 智能合约的升级
interface的意义.https://medium.com/@nrchandan/interfaces-make-your-solidity-contracts-upgradeable-74cd1646a717


另外interface提供了一种便捷升级的方式.(升级需要考虑的一个问题是智能合约的数据如何迁移?新的智能合约需要兼容旧的智能合约)