import java.net.*;
import java.io.*;
public class Client 
{
    public static void main(String[] args) 
	{
	 
	    DatagramSocket cs = null;
        FileOutputStream fos = null;
        int consignment_no=0;
        String ack;
        byte[] q; 
        byte re[];
        String data;
        String data_array[]=new String[10000];
        int pre_consignment;
        int next_con=0;
        int count=0;
        byte[] data_ar;byte[][] arr=new byte[300000][1000]; 
        String file_name=new String(args[2]);
       // System.out.println(test);
        try 
		{
            cs = new DatagramSocket();
            cs.setSoTimeout(3000);
			byte[] rd, sd;
			String reply;
			DatagramPacket sp,rp;
			//int count=1;
			boolean end = false;
            fos = new FileOutputStream("recieved"+file_name);
            
            String request=new String("REQUEST"+file_name+"CRLF");
            //System.out.println(request.length());
            //System.out.println(request.getBytes().length);
            
            
            Send(cs, InetAddress.getByName(args[0]) ,Integer.parseInt(args[1]),request.getBytes());
            int c=0;


            while(true)
            {
                data_ar=Recieve(cs);
                data=new String(data_ar);
                
                while(data.equals("Send Again"))
                {
                    data_ar=Recieve(cs);
                    data=new String(data_ar);
                }
                
                consignment_no=Get_consignment_no(data_ar);
                System.out.println("Recived consignment"+consignment_no);
                
                if(data.substring((data.length()-7),(data.length()-4)).equals("END"))
                {
                     data=new String(data.substring((data.length()-519),(data.length()-7)));
                     arr[consignment_no]=data_ar;
                     data_array[consignment_no]=data;
                     Send(cs, InetAddress.getByName(args[0]),Integer.parseInt(args[1]),("ACK"+consignment_no+"CRLF").getBytes());
                     System.out.println("Sent ack="+consignment_no);

                     break;
                }
                
                data=new String(data.substring(data.length()-512));
                data_array[consignment_no]=data;
                arr[consignment_no]=data_ar;
                Send(cs, InetAddress.getByName(args[0]),Integer.parseInt(args[1]),("ACK"+consignment_no+"CRLF").getBytes());
                System.out.println("Sent ack="+consignment_no);





            }


            
                           
              int ctr=0;
             
              for(int  i=0;i<arr.length;i++)
              {
                  String str = new String(arr[i]) ; 
  
              if(str.length()!=1000)
              {
                  ctr++;            
              }
              }
              
              byte[][] final_array=new byte[ctr][];
              for(int  i=0;i<final_array.length;i++)
              {
                  final_array[i]=arr[i];
              }
              
              byte[][] actual=Reverse(final_array);
            
              
              for(int i=0;i<actual.length;i++)
              {
               fos.write(actual[i]);   
              }





















            // for(int i=0;i<data_array.length;i++)
            // {
            //     if(data_array[i]!=null)
            //     {
            //         count=count+1;
            //     }else{break;}
            // }
            // String final_array[]=new String[count];
            // for(int i=0;i<count;i++)
            // {
            //     //System.out.println(data_array[i].getBytes().length);
            //     final_array[i]=data_array[i];
            // }
            
            
            
            // for(int i=0;i<count;i++)
            // {
            //     fos.write(final_array[i].getBytes());
            // }
            

            
           
            
























        } catch (IOException ex) {
			System.out.println(ex.getMessage());

		} finally {

			try {
				if (fos != null)
					fos.close();
				if (cs != null)
					cs.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
			
			
    }

    public static void Send(DatagramSocket cs,InetAddress ip,int port,byte [] s)
    {
        DatagramPacket sp;
        byte[] sd;
        try{
        
        sp=new DatagramPacket(s,s.length,ip,port);
        cs.send(sp);
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            }
        
    }
    public static byte[] Recieve(DatagramSocket cs)
    {
        DatagramPacket rp;
        byte[] rd;
        rd=new byte[550];
        String r="";
        byte[] ret;
        String delimiter="";
        //byte back=new byte[512];
        byte re[]; 
        byte data_ar[];
        try
        {
            rp = new DatagramPacket(rd,rd.length);
            cs.receive(rp);
            r=new String(rp.getData());
            
            data_ar=rp.getData();
            
            data_ar=byte_arr_trimmer(data_ar);
            
            r=new String(r.trim());
            
            re=rp.getData();
            
            delimiter=new String(r.substring((r.length()-7),(r.length()-4)));
           
            if(delimiter.equals("END"))
                {
                   
                   ret=data_ar;

                }
                else
                {
                   
                  //2System.out.println("RDT"+)
                  ret=data_ar;
                   
                }

        
        
        
        
        }catch (Exception ex){
            ret="Send Again".getBytes();
            //System.out.println(ex.getMessage());
            }
            
            return ret;
    }
    public static int Get_consignment_no(byte[] arr)
    {
        String test;
        int i;
        String s=new String(arr);
        if(!s.equals("Send Again"))
        {
        if(s.substring((s.length()-7),(s.length()-4)).equals("END"))
        {
            
            test=new String(s.substring(3,s.length()-519));
            
        }else
        {
            test=new String(s.substring(3,s.length()-516));
        }
    
        
        i=Integer.parseInt(test);
    }
    else
    {
     i=-1;
    }
        
        return i;
    }

public static byte [] byte_arr_trimmer(byte[] arr)
    {
        String s=new String(arr);
        s=s.trim();
        int len=s.length();
        byte[] arr1=new byte[len];
        for(int i=0;i<arr1.length;i++)
        {
            arr1[i]=arr[i];
        }
        return arr1;
    }
    public static byte[][] Reverse(byte[][] arr)
    {
     byte[] arr1=new byte[512];
     int c=0;
     byte[][] n_a=new byte[arr.length][512];
    
     for(int j=0;j<arr.length;j++)
    {
        
        if(j!=(arr.length-1))
        {
            c=0;
            
            for(int i=arr[j].length-516;i<arr[j].length-4;i++)
            {
                arr1[c]=arr[j][i];
               
               n_a=copier(n_a, arr1, j);
                c=c+1;
            }
           
        }
        else
        {
            break;
        }


    }
    return n_a;
}
    public static byte[][] copier(byte[][] arr,byte[] arr1,int j)
    {
        for(int i=0;i<512;i++)
        {
            arr[j][i]=arr1[i];
        }
        return arr;
    }





























}