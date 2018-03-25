# ERC20
在https://github.com/ethereum/EIPs/blob/master/EIPS/eip-20-token-standard.md   查看ERC20代币的标准API。

### Method

在Method目录下面我们可以看到一些方法，所有的ERC20代币都是按照下面这些方法来定义的。下面我们讲解一下每个方法的作用。

##### 1.name
```
function name() constant returns (string name) 
```
返回string类型的ERC20代币的名字，例如：StatusNetwork

##### 2.symbol
```
function symbol() constant returns (string symbol)
```
返回string类型的ERC20代币的符号，也就是代币的简称，例如：SNT。
##### 3.decimals
```
function decimals() constant returns (uint8 decimals)
```
支持几位小数点后几位。如果设置为3。也就是支持0.001表示。

##### 4.totalSupply
```
function totalSupply() constant returns (uint256 totalSupply)
```
发行代币的总量，可以通过这个函数来获取。所有智能合约发行的代币总量是一定的，totalSupply必须设置初始值。如果不设置初始值，这个代币发行就说明有问题。

##### 5.balanceOf
```
function balanceOf(address _owner) constant returns (uint256 balance)
```
输入地址，可以获取该地址代币的余额。

##### 6.transfer
```
function transfer(address _to, uint256 _value) returns (bool success)
```
调用transfer函数将自己的token转账给_to地址，_value为转账个数
##### 7.approve
```
function approve(address _spender, uint256 _value) returns (bool success)
```

批准_spender账户从自己的账户转移_value个token。可以分多次转移。

##### 8.transferFrom

```
function transferFrom(address _from, address _to, uint256 _value) returns (bool success)
```
与approve搭配使用，approve批准之后，调用transferFrom函数来转移token。

##### 9.allowance

```
function allowance(address _owner, address _spender) constant returns (uint256 remaining)
```
返回_spender还能提取token的个数。


approve、transferFrom及allowance解释：

账户A有1000个ETH，想允许B账户随意调用100个ETH。A账户按照以下形式调用approve函数approve(B,100)。当B账户想用这100个ETH中的10个ETH给C账户时，则调用transferFrom(A, C, 10)。这时调用allowance(A, B)可以查看B账户还能够调用A账户多少个token。


approve是批准，transferFrom是去取，transfer是给。基本交易只是涵盖这两个动作。

### Event

##### 1.Transfer
```
event Transfer(address indexed _from, address indexed _to, uint256 _value)
```
当成功转移token时，一定要触发Transfer事件，另外transferFrom也是返回的transfer event。

##### 2.Approval

```
event Approval(address indexed _owner, address indexed _spender, uint256 _value)
```

当调用approval函数成功时，一定要触发Approval事件.

### ERC20例子

```
pragma solidity ^0.4.18;


contract ERC20 {
    function totalSupply() public constant returns (uint supply);
    function balanceOf( address who ) public constant returns (uint value);
    function allowance( address owner, address spender ) public constant returns (uint _allowance);

    function transfer( address to, uint value) public returns (bool ok);
    function transferFrom( address from, address to, uint value) public returns (bool ok);
    function approve( address spender, uint value ) public returns (bool ok);

    event Transfer( address indexed from, address indexed to, uint value);
    event Approval( address indexed owner, address indexed spender, uint value);
}    
    

contract MyToken is ERC20 {

    string public name;
    string public symbol;
    uint8  public decimals = 8;

    address _cfo;
    mapping (address => uint256) _balances;
    uint256 _supply;
    mapping (address => mapping (address => uint256)) _approvals;
    
    function MyToken (
        uint256 initialSupply,
        string tokenName,
        string tokenSymbol
        ) 
        public 
    {
        _cfo = msg.sender;
        _supply = initialSupply * 10 ** uint256(decimals);  
        _balances[_cfo] = _supply;
        
        name = tokenName;
        symbol = tokenSymbol;
    }

    modifier onlyCFO() {
        require(msg.sender == _cfo);
        _;
    }
    
    function totalSupply() public constant returns (uint256) {
        return _supply;
    }

    function balanceOf(address src) public constant returns (uint256) {
        return _balances[src];
    }
    
    function allowance(address src, address guy) public constant returns (uint256) {
        return _approvals[src][guy];
    }
    
    function transfer(address dst, uint wad) public returns (bool) {
        assert(_balances[msg.sender] >= wad);
        
        _balances[msg.sender] = _balances[msg.sender] - wad;
        _balances[dst] = _balances[dst] + wad;
        
        Transfer(msg.sender, dst, wad);
        
        return true;
    }
    
    function transferFrom(address src, address dst, uint wad) public returns (bool) {
        assert(_balances[src] >= wad);
        assert(_approvals[src][msg.sender] >= wad);
        
        _approvals[src][msg.sender] = _approvals[src][msg.sender] - wad;
        _balances[src] = _balances[src] - wad;
        _balances[dst] = _balances[dst] + wad;
        
        Transfer(src, dst, wad);
        
        return true;
    }
    
    function approve(address guy, uint256 wad) public returns (bool) {
        _approvals[msg.sender][guy] = wad;
        
        Approval(msg.sender, guy, wad);
        
        return true;
    }

}
```

智能合约和java代码有点不同的是public写在后面。


当function中带有public constant时,返回的是具体的数值,对于交易transfer和approve transferFrom没有public constant参数.

### 总结

如果项目方要在以太坊上发行代币来进行融资，一定会按照这个标准来实现相应的函数。
