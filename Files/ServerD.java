import java.net.*;
import java.io.*;
//import java.util.*;
public class ServerD
{   
    
    public static byte[][] chopper(String fname)
    {
        FileInputStream fis = null;
        byte[] bada;
        int result=0;
        int count=0;
        int c=0;
        byte chota[][];
        //long size;
        chota=new byte[10][10];
            
        try
        {
            fis = new FileInputStream(fname);
            long size=fis.getChannel().size();
           // System.out.println(size);
            int fsize = (int) size;
            fsize=fsize/512;
            fsize=fsize+1;
           // System.out.println(fsize);



            chota=new byte[fsize][512];   
            
            
            
            
            
            while(result!=-1)
            {  
                bada=new byte[512];
                result = fis.read(bada);
                if(result!=-1)
                {
                chota[c]=bada;
                c++;
                }
                else
                {
                    break;
                }
            }
        }
        catch (IOException ex) 
        {
            System.out.println(ex.getMessage());
        }
        finally 
        {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}

        }
        return chota;
    }
    
    public static byte[][] GetCon(byte arr[][])
    {
        String rdtseq;
        byte payload[];
        

        byte t[];
        byte t1[];        
        byte arr1[][]=new byte[arr.length][580];
        for(int i=0;i<arr.length;i++)
        {   
            
            if(i!=arr.length-1)
            {
            payload=arr[i];
            rdtseq=new String("RDT"+i);
            t=Concat(rdtseq.getBytes(),payload);
            t1=Concat(t,"CRLF".getBytes());
            arr1[i]=t1;
            }
            
            else
            
            {
            payload=arr[i];
            rdtseq=new String("RDT"+i);
            t=Concat(rdtseq.getBytes(),payload);
            t1=Concat(t,"ENDCRLF".getBytes());
            arr1[i]=t1;
            }
        }
        return arr1;
        
    }
    
    public static byte[] Concat(byte a[],byte b[])
    {
        byte[] result = new byte[a.length + b.length]; 
        System.arraycopy(a, 0, result, 0, a.length); 
        System.arraycopy(b, 0, result, a.length, b.length); 
        return result;
    }
    
    public static String Recieve(DatagramSocket ss)
    {
        String fname;
        String r="";
        try
        {
        byte rd[]=new byte[100];
        DatagramPacket rp;
        rp = new DatagramPacket(rd,rd.length);
        ss.receive(rp);
        fname=new String(rp.getData());
        //System.out.println("FILENAME=== " + fname.trim().substring(7,(fname.trim().length()-4)));
        r=new String(fname.trim().substring(7,(fname.trim().length()-4)));
        }
        catch (Exception ex)
        {
        r="Send Again";
        System.out.println(ex.getMessage());
        }
        return r;
    }
    public static int Recieve_ack(DatagramSocket ss)
    {
        String ack;
        String r="";
        int i=0;
        try{
        
        byte rd[]=new byte[6000];
        DatagramPacket rp;
        rp = new DatagramPacket(rd,rd.length);
        ss.receive(rp);

        ack=new String(rp.getData());
        //System.out.println(ack.length());
        //System.out.println(ack);
        r=new String(ack.trim());
       // System.out.println(r.length());
        r=new String(r.substring(3,r.length()-4));
       // System.out.println(r);
        
    }catch (Exception ex)
    {
        i=-1;
    }
      
            if(i>=0)
            {
            i=Integer.parseInt(r);
            }
            return i;
        
        }
    
    public static void Send(DatagramSocket ss,InetAddress ip,int port,byte [] s)
    {
        DatagramPacket sp;
        byte[] sd;
        try{
        sd=s;
        sp=new DatagramPacket(sd,sd.length,ip,port);
        ss.send(sp);
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            }
        
    }
    
    public static void DSend(DatagramSocket ss,InetAddress ip,int port,String s)
    {
        DatagramPacket sp;
        byte[] sd;
        try{
        sd=s.getBytes();
        sp=new DatagramPacket(sd,sd.length,ip,port);
        //ss.send(sp);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            }
        
    }

    
    
    
    
    public static void main(String[] args) 
    {
        DatagramSocket ss = null;
        InetAddress ip;
        int port;
        byte consignment[][];
        String fname;
        byte arr[][];
        int ack=0;
       // String data;
        int ack_no=0;
        int ctr=0;
        int d_p_1=Integer.parseInt(args[1]);
        int d_p_2=Integer.parseInt(args[2]);
        int d_p_3=Integer.parseInt(args[3]);
       try 
		{
			ss = new DatagramSocket(Integer.parseInt(args[0]));
            System.out.println("Server is up....");
            ss.setSoTimeout(3000);
           
            byte rd[]=new byte[530];
            byte test[];//=new byte[20];
            //byte test1[];
            DatagramPacket rp;
            rp = new DatagramPacket(rd,rd.length);
            ss.receive(rp);
            fname=new String(rp.getData());
            byte test1[]=new byte[fname.trim().length()];
            //System.out.println(rp.getData().length);
            //System.out.println(fname.trim().length());
            test=rp.getData();
            test1=test;
            int c=0;
            for(int i=0;i<test1.length;i++)
            {
                if(test1[i]!=0)
                {
                    c=c+1;
                }
                //System.out.println(test1[i]);
            }
            
            fname=new String(test1);
            
            
            
            System.out.println("FILENAME=== " + fname.trim().substring(7,(fname.trim().length()-4)));
           
            ip = rp.getAddress(); 
            port =rp.getPort();

            arr=chopper(fname.trim().substring(7,(fname.trim().length()-4)));
            consignment=GetCon(arr);
            
            while(true)
            {
                try{
                byte[] data=new byte[consignment[ack].length];
                data=consignment[ack];
                if((ack==d_p_2 ||ack== d_p_1||ack==d_p_3) && ctr==0)
                {
                    
                    System.out.println("Packet Dropped"+ack);
                    System.out.println();
                    ctr=ctr+1;
                    
                }
                else
            {
                Send(ss, ip, port, data);
                System.out.println("Sent Consignment No"+ack);
                ack_no=Recieve_ack(ss);
                while(ack_no==-1)
                {
                    Send(ss, ip, port, data);
                    System.out.println("Sent Consignment No"+ack);
                    ack_no=Recieve_ack(ss);
                }
            
                System.out.println("Recived ack"+ack_no);
                ack=ack_no+1;
                ctr=0;
            }
            }
            catch(Exception ex)
            {
                System.out.println("END");
                break;
            }

            }
            
            
            
            
           
            

           

           
           

                
           
            
            
            


            
            











































        } catch (IOException ex) {
			System.out.println(ex.getMessage());

		}finally{
            ss.close();
        }
			
		
        
        
        
        
        
    }
        





}
    






