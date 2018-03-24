# 关键代码说明

这篇文档讲主要说明在erc20-rest-service中比较关键核心的代码,分别是
- 生成的智能合约及web3j.
- solidity编写的智能合约.

### 生成的智能合约及web3j
生成的智能合约及web3j主要针对以下几个部分展开:
- RemoteCall 函数调用方式
- 基于RxJava的异步消息通信事件机制

在web3j中有一些关键的类以及围绕这些类实现的方法,主要有:
- Contract
- ManagedTransaction
- TransactionReceipt
- RemomteCall


##### RemoteCall函数调用方式


##### 基于RxJava的异步消息通信事件机制


### solidity编写的智能合约
