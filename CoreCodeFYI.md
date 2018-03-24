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

1. Controller中主要包含一下几个函数
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

2. 当controller接受到请求后,会调用contractService中的某些方法去执行相应的操作.在contractService中,按照上面两类分类,一共有以下两种函数:
- 非transaction类型.直接传给HumanStandardToken中,调用send()方法执行.
- transcation类型.先传给HUmanStandardToken中,调用send()方法执行,执行后通过返回的TransactionReceipt去获取相应的event.

需要说明的是,目前连接以太坊测试网络的infura暂不支持通过RxJava获得订阅的事件,另外在java服务器端不断获取订阅的事件是否具有实用性价值后续再考虑.

3. 接下来说明HumanStandardToken.

HumanStandardToken通过solc和web3j工具动态生成,在生成的java代码中,包含了三类比较重要的函数,罗列如下:
- RemoteCall<String> name()  RemoteCall<TransactionReceipt> transfer()
- List<ApprovalEventResponse> getApprovalEvents((TransactionReceipt transactionReceipt))
- Observable<ApprovalEventResponse> approvalEventObservable.
  
  第三个方法提供了针对某种事件的可观察者,由于目前不支持,略过不提.第二个方法根据TransactionReceipt获取相应的event,在ContractService中被调用.

重点说明第一个方法.

RemoteCall定义如下:
```
public class RemoteCall<T> {

    private Callable<T> callable;

    public RemoteCall(Callable<T> callable) {
        this.callable = callable;
    }

    /**
     * Perform request synchronously.
     *
     * @return result of enclosed function
     * @throws Exception if the function throws an exception
     */
    public T send() throws Exception {
        return callable.call();
    }

    /**
     * Perform request asynchronously with a future.
     *
     * @return a future containing our function
     */
    public CompletableFuture<T> sendAsync() {
        return Async.run(this::send);
    }

    /**
     * Provide an observable to emit result from our function.
     *
     * @return an observable
     */
    public Observable<T> observable() {
        return Observable.create(
                subscriber -> {
                    try {
                        subscriber.onNext(send());
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
        );
    }
}
```
上面代码中,observable是一个被观察对象,略过不提.

sendAsync是为了自定义使用,可以略过其实现.

重点是RemoteCall没有直接集成Callable,而是包含了一个Callable的例子.Callable在RemoteCall的初始化过程中实现.

查看callable,callable是一个由于RemoteCall中的send函数会直接调用callable的call方法,因此还需要构造参数中实现call方法.callable代码如下:
```
@FunctionalInterface
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

在web3j中,实现方式采用lamda表达式.代码如下:
```
new RemoteCall<>(() -> executeTransaction(function))
```
lamda表达式可以简单表达匿名函数,此即函数式编程.


##### 基于RxJava的异步消息通信事件机制

Rxjava的出现是为了优雅的实现异步通信.异步通信有多种方案,并非非rxjava不可.

rxjava和lambda表达式结合,可以写出非常优雅的异步通信例子.

在这个项目中使用Rxjava比较少,Rxjava主要是使用发布-订阅机制来进行相应的处理.

另外在web3的代码实现中,使用了大量的泛型类/泛型接口/泛型方法.

### solidity编写的智能合约
