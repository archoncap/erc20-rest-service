# 关键代码说明


这篇文档讲主要说明在erc20-rest-service中比较关键核心的代码,分别是
- 生成的智能合约及web3j.
- solidity编写的智能合约.

### 生成的智能合约及web3j


生成的智能合约及web3j主要针对以下几个部分展开:
- RemoteCall 函数调用方式
- 基于RxJava的异步消息通信事件机制

ps:web3j大量使用了java1.8的新特性,例如lamda表达式,函数式编程表现非常明显.
web3j异步通信主要使用okhttp,事件机制主要使用RxJava.


在web3j中有一些关键的类以及围绕这些类实现的方法,主要有:
- Contract
- TransactionReceipt
- RemomteCall

其中生成的智能合约直接继承web3j.jar的抽象类.在contract中实现了非常多的与底层进行交互的方法,例如executeCallSingleValueReturn,executeTransaction等.
TranscationRecipt 是基本的数据结构,用作接受返回的数据.
RemoteCall是包含Callable的类,所有的异步通信请求均从这里发出.

下面针对两个主要的模块进行说明.

##### RemoteCall函数调用方式

Controller中主要包含一下几个函数
- deploy
- name
- totalSupply
- decimals
- transfer
- allowance
- approveAndCall
- symbol
- balanceOf
- approve
- transferFrom

controller面向的函数主要分为两类,一类是非transaction的查询操作,一类是查询操作.

当web服务接受到controller请求后,会单独开一个线程进行处理,中途可能由于网络通信该线程被block.在服务器中,每秒的线程可能有上千个,而每一个时间点运行的线程小于等于内核的个数,线程的切换需要时间.go比java并发高的原因在于协程切换成本低,后话不提.

当controller接受到请求后,会调用contractService中的某些方法去执行相应的操作.在contractService中,按照上面两类分类,一共有以下两种函数:
- 非transaction类型.直接传给HumanStandardToken中,调用send()方法执行.
- transcation类型.先传给HUmanStandardToken中,调用send()方法执行,执行后通过返回的TransactionReceipt去获取相应的event.

需要说明的是,目前连接以太坊测试网络的infura暂不支持通过RxJava获得订阅的事件,另外在java服务器端不断获取订阅的事件是否具有实用性价值后续再考虑.

接下来需要说明HumanStandardToken.


##### 基于RxJava的异步消息通信事件机制


### solidity编写的智能合约
