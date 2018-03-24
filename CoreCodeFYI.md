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



##### 基于RxJava的异步消息通信事件机制


### solidity编写的智能合约
