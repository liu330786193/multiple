package com.lyl.pan;import com.dcfs.esb.ftp.client.https.FtpPut;import com.dcfs.esb.ftp.server.msg.FileMsgBean;import com.pabank.sdk.PABankSDK;public class HttpPutTestAdd {	public static void main(String[] args) {//		new FtpPutTestAdd().testAdd(args[0], args[1]);		new HttpPutTestAdd().testAdd("/tler/FTP6.zip","D:/FTP/FTP11.zip");	}	public void testAdd(String remoteFile,String localFile) {		FtpPut ftpPut = null;		FileMsgBean bean = null;		try {			//指定配置文件的路径			PABankSDK.init("conf/config.properties");			//验证开发者			PABankSDK.getInstance().approveDev();						bean = new FileMsgBean();			bean.setContinueFlag(true);			/** FtpPut类的构造器使用说明			 参数一》localFile:		需要上传的本地文件的绝对路径,如"D:/down/flieTest/报价单.txt"			 参数二》 remoteFile： 	上传到文件服务器的文件所在的相对路径，需要带上用户目录，如"/picp/报价单.txt"，picp是文件服务器分配的用户名			 参数三》 scrtFlag ：	是否加密标志，填false			 参数四》 key：			加密密钥，填null			 参数五》 bean：			消息bean，用于获取服务器响应的消息			 * */			ftpPut = new FtpPut(localFile, remoteFile,false,null,bean);			//使用指定签约//			ftpPut = new FtpPut(localFile, remoteFile,false,null,bean,"BBB");			ftpPut.doPutFile();						//文件路径需要通过报文的方式发送到文件下载方			System.out.println("服务器文件路径=["+bean.getFileName()+"]");			//私密授权码需要通过报文的方式发送到文件下载方			System.out.println("服务器文件的私密授权码=["+bean.getPrivateAuth()+"]");		} catch (Exception e) {			//捕获到异常后，可以从FileMsgBean类获取异常信息			System.out.println(bean.getFileRetMsg());			e.printStackTrace();		} finally{			ftpPut.close(true);		}	}}