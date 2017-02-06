Java 实现断点续传的关键几点
(1) 用什么方法实现提交 RANGE: bytes=2000070-。 

当然用最原始的 Socket 是肯定能完成的，不过那样太费事了，其实 Java 的 net 包中提供了这种功能。代码如下： 

URL url = new URL("http://www.sjtu.edu.cn/down.zip"); 
HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection(); 

// 设置 User-Agent 
httpConnection.setRequestProperty("User-Agent","NetFox"); 
// 设置断点续传的开始位置 
httpConnection.setRequestProperty("RANGE","bytes=2000070"); 
// 获得输入流 
InputStream input = httpConnection.getInputStream(); 
从输入流中取出的字节流就是 down.zip 文件从 2000070 开始的字节流。 大家看，其实断点续传用 Java 实现起来还是很简单的吧。 接下来要做的事就是怎么保存获得的流到文件中去了。

(2) 保存文件采用的方法。
 
我采用的是 IO 包中的 RandAccessFile 类。 
操作相当简单，假设从 2000070 处开始保存文件，代码如下： 
RandomAccess oSavedFile = new RandomAccessFile("down.zip","rw"); 
long nPos = 2000070; 
// 定位文件指针到 nPos 位置 
oSavedFile.seek(nPos); 
byte[] b = new byte[1024]; 
int nRead; 
// 从输入流中读入字节流，然后写到文件中 
while((nRead=input.read(b,0,1024)) > 0) 
{ 
oSavedFile.write(b,0,nRead); 
}
怎么样，也很简单吧。 接下来要做的就是整合成一个完整的程序了。包括一系列的线程控制等等。
