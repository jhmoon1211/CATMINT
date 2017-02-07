import socket
import MySQLdb
import time
import random
from subprocess import call

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


def match_direction(input_string):
    global pan_pos1
    global pan_pos2
    call_str = ""
    
    if input_string =="right":
        pan_pos1 += 10
        if pan_pos1 >= max_pos:
            pan_pos1 = max_pos
            
        input_string = "servo left"
        call_str = "echo 5="+str(pan_pos1)+"> /dev/servoblaster"

    
    elif input_string == "left":
        pan_pos1 -= 10
        if pan_pos1 <= min_pos:
            pan_pos1 = min_pos

        input_string = "servo right"
        call_str = "echo 5="+str(pan_pos1)+"> /dev/servoblaster"

    elif input_string == "up":
        pan_pos2 += 10
        if pan_pos2 >= max_pos:
            pan_pos2 = max_pos

        input_string = "servo up"
        call_str = "echo 6="+str(pan_pos2)+"> /dev/servoblaster"

    elif input_string == "down":
        pan_pos2 -= 10
        if pan_pos2 <= min_pos:
            pan_pos2 = min_pos

        input_string = "servo down"
        call_str = "echo 6="+str(pan_pos2)+"> /dev/servoblaster"

    elif input_string == "clear":
        pan_pos1 = 55
        pan_pos2 = 55

        input_string = "servo clear"
        call_str = "echo 5="+str(pan_pos1)+"> /dev/servoblaster | echo 6="+str(pan_pos2)+"> /dev/servoblaster"

    elif input_string == "auto":
        get_pattern()
        input_string="auto"                                                                       
        print('auto')

    elif input_string =="play":
        input_string="play"
        print('play')
        
    else :
        input_string = input_string+"dont have"
        
    return call_str



def get_pattern():
    global str1
    global str2
    multi_strs = ["", ""]
    
    db = MySQLdb.connect(host="192.168.0.88", user="root", passwd="catmint", db="catmint")
    db.set_character_set('utf8mb4')

    cursor = db.cursor()

    sql = "SELECT pattern"+str(random.randint(1,4))+" from basicpattern"
    print(sql)

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
                
                for i in range(0, len(data)):
                    multi_strs[i] = match_direction(data[i])

                call(multi_strs[0]+" | "+multi_strs[1], shell= True)
                time.sleep(1)
                
            else:
                match_direction(datas[i])
                time.sleep(1)
        
        
    except Exception as e:
        print(str(e))
        db.rollback()

        db.close()

        
while True :
    conn, addr = s.accept()
    print("Connected by ", addr)

    data = conn.recv(1024)
    data = data.decode("utf8").strip()
    if not data : break
    print("Received: "+data)

    live_str = match_direction(data)
    call(live_str, shell= True)

    conn.sendall(live_str.encode("utf-8"))

    conn.close()
    
s.close()
                    
