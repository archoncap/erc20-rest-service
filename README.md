# erc20-rest-service

This is a sample ethereum starter project.

In this project , We will issue an erc20 token.

### architecture

开发架构层次如下图:

![Screenshot from 2018-03-23 18-43-22.png](https://upload-images.jianshu.io/upload_images/6907217-20688aae29e9cb5d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在上图中，用颜色标记出来的区域是需要进行相应的开发实现。主要包括两个部分：
- 智能合约的开发和测试，需要编写的文件有contract和test文件。
- java的整合智能合约层，需要编写的文件有service和controller以及ethereum connector层。

 上图中的灰色部分是暂时为理解其原理的部分.
 
 ### 智能合约层
 负责实现相应的智能合约业务，编写测试实例。

 在开发中基于ganache进行开发调试，需要智能合约的开发者具有较好的js基础。

 在编写智能合约的过程中，需要熟练contract和test的语法。


 ### 整合智能合约层

 从图中可以看出，整合智能合约层主要分为三个部分：
 - 自动生成的智能合约，通过web3命令行工具可以将solidity的智能合约自动转换（auto-generated）为java的智能合约，并提供deploy和call等函数。
 - web3j的connector。通过web3j实现了与智能合约的连接，包括整合web3j实现与以太坊网络的通信，部署智能合约和智能合约内部函数的调用。
 - controller和service层的实现。作为智能合约和前端的桥接。

 为了加快开发，这个项目引用了infura来实现智能合约快速在rinkeby部署和调用。

开发结束后,可以在https://www.rinkeby.io/#explorer查看相应的交易细节.


 ### 项目依赖
 - Python 3.5+ 
 - solc(ubuntu下通过apt-get install安装，非npm包管理安装)
 - web3（需要写入到系统环境变量中，负责自动生成java智能合约代码和提供api调用）

 ### 项目目录

项目导入intellij后可以立即运行，并暴露出8080端口提供restful api供数据访问。

 项目src下面主要包括两个重要的文件夹,其中
 - resource/contract 是存放智能合约与相应测试的文件夹，使用truffle进行编写。
 - main 下存放着相应的java代码。

 附件中主要重要的脚本：
 - generate.sh 自动生成智能合约的Bin和abi的文件,以及在指定目录生成智能合约的相应代码.

 ### 功能模块设计

 在项目中实现了基本的erc20的例子,包括智能合约的实现和java代码的实现.

 为了方便查看与调用，在项目中配置swagger界面。http://localhost:8080/swagger-ui.html
![Screenshot from 2018-03-24 10-14-50.png](https://upload-images.jianshu.io/upload_images/6907217-466d0aeaf7b099c0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

 在swagger-ui中调用相应的接口,可以在rinkeby testnet上查看到交易细节.
