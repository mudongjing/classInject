# classInject
一个注解，用于将指定的java包下的所有类在编译期间写入当前文件中，作为额外的属性字段。

例如：

> java包`org.example.test`下存在几个实体类，我们希望作为另一个类的字段
>
> 假设这个包下有`User`和`Student`类，在另一个包下有一个`Test`类需要它们，
>
> ```java
> @InjectClass(path="org.example.test")
> public class Test {
> }
> ```
>
> 项目编译成功后，`Test.class`文件内部变为
>
> ```java
> import org.example.test.Student;
> import org.example.test.User;
> 
> public class Test {
>     private Student student;
>     private User user;
> 
>     public User getUser() {
>         return this.user;
>     }
> 
>     public void setUser(User user) {
>         this.user = user;
>     }
> 
>     public Student getStudent() {
>         return this.student;
>     }
> 
>     public void setStudent(Student student) {
>         this.student = student;
>     }
> 
>     public Test() {
>     }
> }
> ```

本项目主要参考并使用了https://github.com/houbb/lombok-ex项目中封装的一些类和方法。

本人主要使用maven项目，其它读者如果需要用的话，可以在自己的本地maven仓库下，建立目录`io\github\mudongjing\classInject\1.0`，下载 `classInject-1.0.zip`文件到该目录下解压。

```
<dependency>
    <groupId>xyz.wcd</groupId>
    <artifactId>classInject</artifactId>
    <version>1.0</version>
</dependency>
```

