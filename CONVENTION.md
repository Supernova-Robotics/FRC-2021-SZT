# Supernova Robotics Coding Convention

这篇文档规定了 Supernova Robotics 的代码格式规范

## 缩进

我们使用空格而不是制表符 (Tab) 来进行缩进，并对于不同语言我们使用不同的缩进单位

| 缩进 | 语言 |
| ---- | ---- |
| 2 空格 | C, C++, Java, HTML, JS |
| 4 空格 | Python |

## 每行长度

将每行代码限制在 80 字符内

## 对于跨行操作符

对于跨行操作符，将操作符放在每个新行的起始位置：

###### 正确

``` Python
income = (gross_wages
          + taxable_interest
          + (dividends - qualified_dividends)
          - ira_deduction
          - student_loan_interest)
```

###### 错误

``` Python
income = (gross_wages +
          taxable_interest +
          (dividends - qualified_dividends) -
          ira_deduction -
          student_loan_interest)
```

## 空行

在类定义前后插入两行空行

在方法定义前后插入一行空行

可以用额外的空行来区别具有相似性的一组方法或类

## 括号换行

我们使用如下格式

###### 正确

``` C
int doSomething(double a, int b);

int doSomething(double a, int b) {
  if (condition) {
    // do something...    
  }
  else {
    // do something...
  }
}
```

###### 错误

``` C
int doSomething (double a, int b);

int doSomething (double a, int b)
{
  if (condition)
  {
    // do something...    
  } else {
    // do something...
  }
}
```


## 空白

对于空白的使用，我们遵循如下方法，在保证可读性的同时尽量减少空白字符的使用数量：

- 避免在括号后和反括号前插入空白

  ###### Correct

  ```Python
  doSomething(ham[1], {eggs: 2})
  ```

  ###### Wrong

  ```Python
  doSomething( ham[ 1 ], { eggs: 2 } )
  ```

- 避免在相邻的逗号和反括号之间插入空白

  ###### Correct

  ```Python
  foo = (0,)
  ```

  ###### Wrong

  ```Python
  bar = (0, )
  ```

- 避免在逗号，分号或冒号前插入空白

  ###### Correct

  ```Python
  if x == 4: print x, y; x, y = y, x
  ```

  ###### Wrong

  ```Python
  if x == 4 : print x , y ; x , y = y , x
  ```

- 避免在方法或类型名后插入空白

  ###### Correct

  ```Python
  doSomething(1, 2)
  ```

  ###### Wrong

  ```Python
  doSomething (1, 2)
  ```

- 避免在数组索引和字典键值的括号前插入空白

  ###### Correct

  ```Python
  dct['key'] = lst[index]
  ```

  ###### Wrong

  ```Python
  dct ['key'] = lst [index]
  ```
  
- 避免在类型转换操作符后插入空白

  ###### Correct

  ```C
  uint16_t val_16 = 0;
  uint32_t val_32 = (uint32_t)val_16;
  ```

  ###### Wrong

  ```C
  uint16_t val_16 = 0;
  uint32_t val_32 = (uint32_t) val_16;
  ```

## 注释

尽量避免使用中文字符进行注释

### 单行注释

将注释位置对齐缩进单位

在注释字符和注释内容之间插入一个空白字符

###### Correct

```C
foo = 1;        // This is a comment
```

###### Wrong

```C
foo = 1;        //This is a comment
```

### 文档注释

文档注释遵循 [Javadoc](https://www.oracle.com/hk/technical-resources/articles/java/javadoc-tool.html) 格式

```C
/**
 *  A test class. A more elaborate class description.
 */
class Javadoc_Test {
  public:
 
    /** 
     * An enum.
     * More detailed enum description.
     */
    enum TEnum { 
      TVal1, /**< enum value TVal1. */  
      TVal2, /**< enum value TVal2. */  
      TVal3  /**< enum value TVal3. */  
    } *enumPtr, /**< enum pointer. Details. */
    enumVar;  /**< enum variable. Details. */
       
    /**
      * A constructor.
      * A more elaborate description of the constructor.
      */
    Javadoc_Test();

    /**
      * A destructor.
      * A more elaborate description of the destructor.
      */
    ~Javadoc_Test();
  
    /**
      * a normal member taking two arguments and returning an integer value.
      * @param a an integer argument.
      * @param s a constant character pointer.
      * @see Javadoc_Test()
      * @see ~Javadoc_Test()
      * @see testMeToo()
      * @see publicVar()
      * @return The test results
      */
      int testMe(int a, const char *s);
      
    /**
      * A pure virtual member.
      * @see testMe()
      * @param c1 the first argument.
      * @param c2 the second argument.
      */
      virtual void testMeToo(char c1, char c2) = 0;
  
    /** 
      * a public variable.
      * Details.
      */
      int publicVar;
      
    /**
      * a function variable.
      * Details.
      */
      int (*handler)(int a, int b);
};
```

## 变量名称

### 包/模组/命名空间名称

对于包/模组/命名空间名称，应使用全小写字符，数字和下划线

对于不为用户而设计的包/模组 (例如 Python 向 C 的转接模组)，可以在名称的前方添加下划线加以区分 (例：_socket)

### 类名

类名称应使用 CamelCase 格式

```Python
class SocketServer:
    # ...
```

```Java
public class SocketServer {
  // ...
}
```

### 类型名

类型名称应使用 CamelCase 格式

```C
typedef enum {
  LeftHand,
  RightHand
} JoystickHand;
```

### 方法名

方法名应使用 mixedCase，并以动词作为第一个单词


```Python
def doSomething():
    # ...
    
def parseHTTPArgument():
    # ...
    
def activateProtocol():
    # ...
```

```Java
void doSomething() {
  // ...
}
    
int parseHTTPArgument() {
  // ...
}
    
double activateProtocol() {
  // ...
}
```

### 常量名

使用全大写和下划线 ALL_CAPS 作为常量名

### 变量名

使用全小写和下划线作为变量名

如果方法的参数名和类的变量名称有冲突，应优先考虑使用近义词进行替换，如果没有合适的词语，则应在方法参数名后面加下划线 (例：`value` -> `value_`)

对于类型中的私有变量，应在变量名称前方加下划线 (例：`value` -> `_value`)

## 其他

不要将布尔值和 true/false 来进行比较，布尔值可以直接作为判断条件

###### Correct

```Python
if boolean_value:
    # ...
```
```C
if (boolean_value) {
  // ...
}
```

###### Wrong

```Python
if boolean_value is True:
    # ...
```
```C
if (boolean_value == true) {
  // ...
}
```

### Define Guard

对于 C/Cpp，需要在头文件中使用 define guard 来确保头文件不会被加载多次

define guard 的格式如下

```C
#ifndef FOO_BAR_BAZ_H_
#define FOO_BAR_BAZ_H_

// ...

#endif  // FOO_BAR_BAZ_H_
```