import socket
import MySQLdb
from subprocess import call
import time
#-*- coding: utf-8 -*-

max_pos = 200
min_pos = 55
pan_pos1 = 55
pan_pos2 = 55

HOST = ""
PORT = 8888
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')
s.bind((HOST, PORT))
print('Socket bind complete')
s.listen(1)
print('Socket now listening')

def get_pattern():
    db = MySQLdb.connect(host="192.168.0.88", user="root", passwd="catmint", db="catmint")
    db.set_character_set('utf8mb4')

    cursor = db.cursor()

    sql = "SELECT pattern1 from basicpattern"

    try:
        cursor.execute(sql)
        db.commit()
        rows = cursor.fetchone()
        datas = rows[0].split(" ")
        for i in range(0, len(datas)):
            print(datas[i])

        for i in range(0, len(datas)):
            if datas[i].find(",") != -1:
                data=datas[i].split(",")
                same_time_motor_move(data)
                time.sleep(1)
            else:
                do_some_stuffs_with_input(datas[i])
                time.sleep(1)
        
        
    except Exception as e:
        print(str(e))
        db.rollback()

        db.close()


def same_time_motor_move(data):
    global pan_pos1
    global pan_pos2
    
    for i in range(0, len(data)):
        print(data[i])
        
        if data[i]=="right":
            pan_pos1 += 10
            if pan_pos1 >= max_pos:
                pan_pos1 = max_pos
            str1="5="+str(pan_pos1)+"> /dev/servoblaster"
            
        elif data[i]=="left":
            data[i]="5"
            pan_pos1 -= 10
            if pan_pos1 <= min_pos:
                pan_pos1 = min_pos
            str1="5="+str(pan_pos1)+"> /dev/servoblaster"
            
        elif data[i]=="up":
            data[i]="6"
            pan_pos2 += 10
            if pan_pos2 >= max_pos:
                pan_pos2 = max_pos
            str2="6="+str(pan_pos2)+"> /dev/servoblaster"
            
        elif data[i]=="down":
            data[i]="6"
            pan_pos2 -= 10
            if pan_pos2 <= min_pos:
                pan_pos2 = min_pos
            str2="6="+str(pan_pos2)+"> /dev/servoblaster"

    call("echo "+str1+" | echo "+str2, shell= True)

        

def do_some_stuffs_with_input(input_string):
    global pan_pos1
    global pan_pos2
    
    if input_string =="right":
        pan_pos1 += 10
        if pan_pos1 >= max_pos:
            pan_pos1 = max_pos
            
        input_string = "servo left"
        call("echo 5="+str(pan_pos1)+" > /dev/servoblaster", shell= True)
    
    elif input_string == "left":
        pan_pos1 -= 10
        if pan_pos1 <= min_pos:
            pan_pos1 = min_pos

        input_string = "servo right"
        call("echo 5="+str(pan_pos1)+"> /dev/servoblaster", shell= True)

    elif input_string == "up":
        pan_pos2 += 10
        if pan_pos2 >= max_pos:
            pan_pos2 = max_pos

        input_string = "servo up"
        call("echo 6="+str(pan_pos2)+"> /dev/servoblaster", shell= True)

    elif input_string == "down":
        pan_pos2 -= 10
        if pan_pos2 <= min_pos:
            pan_pos2 = min_pos

        input_string = "servo down"
        call("echo 6="+str(pan_pos2)+"> /dev/servoblaster", shell= True)

    elif input_string == "clear":
        pan_pos1 = 55
        pan_pos2 = 55

        input_string = "servo clear"
        call("echo 5="+str(pan_pos1)+"> /dev/servoblaster | echo 6="+str(pan_pos2)+"> /dev/servoblaster", shell= True)

    elif input_string == "auto":
        get_pattern()
        input_string="auto"
        print('auto')

    elif input_string =="play":
        input_string="play"
        print('play')
        
    else :
        input_string = input_string+"dont have"
    return input_string

while True :
    conn, addr = s.accept()
    print("Connected by ", addr)

    data = conn.recv(1024)
    data = data.decode("utf8").strip()
    if not data : break
    print("Received: "+data)

    res = do_some_stuffs_with_input(data)
    print("move:"+res)

    conn.sendall(res.encode("utf-8"))

    conn.close()
    
s.close()
                    
