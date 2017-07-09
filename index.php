<?php 

Interface Activities{
   public function Deposit($amount);
   public function Withdraw($amount);
}

abstract class User {
   
    public function UserDetails($name,$firstName,$lastName,$email){
        $this->name=$name;
        $this->firstName=$firstName;
        $this->lastName=$lastName;
        $this->email=$email;
       
    }
    public function show()
    {
        echo "<br>Username: ".$this->name;
        echo "<br>First Name: ".$this->firstName;
        echo "<br>Last Name: ".$this->lastName;
        echo "<br>Email: ".$this->email;
      
    }    
   
}

 class Account extends User{
     
     public $sav_amount,$cur_amount;
     
     function __construct($acNo,$balance,$bankCode,$accountType,$ifsc)
     {
         $this->acNo=$acNo;
         $this->balance=$balance;
         $this->bankCode=$bankCode;
         $this->accountType=$accountType;
         $this->ifsc=$ifsc;
         
         if($accountType=="Saving_account")
         {
             $this->sav_amount=$this->balance;
         }
         elseif ($accountType=="Current_account") {
             
             $this->cur_amount=$this->balance;
     }
     }
     
     public function show()
     {
        // echo "<br>Accounts details";
         parent::show();
        echo "<br>A/C No: ".$this->acNo;
        echo "<br>Bank Code: ".$this->bankCode;
        echo "<br>Account Type: ".$this->accountType;
        echo "<br>IFSC Code: ".$this->ifsc;
     }   
     
     
     public function Transfer($code,$amount,$ifsc,$acno)
     {
         
         //Transfer is between current to saving acc
          echo "<br><br><b>Transfer Details</b>";
               if($this->bankCode==$code)
         {
            
             echo "<br><b>Transfer to same bank </b>";
             echo "<br>Amount Transfered: ".$amount;
             echo "<br>Account no. to deposit amount: ".$acno;
         }
        else {
            
              echo "<br><b>Transfer to different bank </b>";
             echo "<br>Amount Transfered: ".$amount;
             echo "<br>Account no. to deposit amount: ".$acno;
             echo "<br>IFSC Code: ".$ifsc;
        }
     }
     
    
 }
 
 class Saving extends Account implements Activities{
     
     
    public function Deposit($amount)
     {
         if($amount>49000)
         {
             echo "<br><br><b> You cannot deposit more than 49000 in your account</b>";
         }
        else {
         $this->sav_amount=$this->sav_amount+$amount;
         echo"<br><br><b>Deposited successfully and amount is :</b>" .$this->sav_amount;
        }
     }
     public function show()
     {
         echo"<br><br><b>Saving Account details</b>";
         parent:: show();
         echo"<br>Saving Balance: ".$this->sav_amount;
     }
  public function Withdraw($amount)
   {
       if($this->sav_amount==0)
       {
           echo"<br><br><b>Your account has insufficient balance</b>";
       }
        else {
         if($amount>10000)
           echo"<br><br><b> You cannot withdraw more than 10000</b>";
        else {
            $this->sav_amount=$this->sav_amount-$amount;
            echo"<br><br><b>Withdraw successfully and amount is :</b>" .$this->sav_amount;
       }
       }
   }
 }

class Current extends Account implements Activities{
     public function show()
     {
         echo"<br><br><b>Current Account details</b>";
         parent:: show();
         echo"<br>Current Balance: ".$this->cur_amount;
     }
    public function Deposit($amount)
     {
         $this->cur_amount=$this->cur_amount+$amount;
         echo "<br><br><b>Amount deposited successfully and your balance is: </b>".$this->cur_amount;
     }
     public function Withdraw($amount)
     {
         $this->cur_amount=$this->cur_amount-$amount;
         echo "<br><br><b>Amount withdrawn successfully and your balance is: </b>".$this->cur_amount;
     }
} 

 
 $a=new Saving(123,5000,"ABC","Saving_account","ABC123");
 $a->UserDetails("Aangi810","Aangi","Mehta","aangimehta@hotmail.com");
 $a->show();
 $a->Deposit(50000);
 $a->Withdraw(10050);
 
 $b=new Current(145,10000,"XYZ","Current_account","XYZ145");
 $b->UserDetails("Mann218","Mann","Mehta","mannmehta@live.com");
 $b->show();

 
 $b->Transfer("ABC",200,"ABC123",123); //Transfer from current to saving
 $b->Withdraw(200);
 $a->Deposit(200);
 $b->show();
 $a->show();
  
 $a->Transfer("ABC",500,"ABC123",123); //Transfer from saving to current
 $a->Withdraw(500);
 $b->Deposit(500);
 
