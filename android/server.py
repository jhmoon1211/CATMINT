import socket
import MySQLdb
import time
import random
import RPi.GPIO as GPIO
from subprocess import call

#-*- coding: utf-8 -*-

max_pos = 200
min_pos = 55
pan_pos1 = 55
pan_pos2 = 55
sound_power = 1
laser_power = False

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
    
    if input_string =="r":
        pan_pos1 += 10
        if pan_pos1 >= max_pos:
            pan_pos1 = max_pos
            
        call_str = "echo 5="+str(pan_pos1)+"> /dev/servoblaster"

    
    elif input_string == "l":
        pan_pos1 -= 10
        if pan_pos1 <= min_pos:
            pan_pos1 = min_pos

        call_str = "echo 5="+str(pan_pos1)+"> /dev/servoblaster"

    elif input_string == "u":
        pan_pos2 += 10
        if pan_pos2 >= max_pos:
            pan_pos2 = max_pos

        call_str = "echo 6="+str(pan_pos2)+"> /dev/servoblaster"

    elif input_string == "d":
        pan_pos2 -= 10
        if pan_pos2 <= min_pos:
            pan_pos2 = min_pos

        call_str = "echo 6="+str(pan_pos2)+"> /dev/servoblaster"

    elif input_string == "clear":
        pan_pos1 = 55
        pan_pos2 = 55

        call_str = "echo 5="+str(pan_pos1)+"> /dev/servoblaster | echo 6="+str(pan_pos2)+"> /dev/servoblaster"

    elif input_string == "line":
        pattern = "pattern1"
        get_pattern(1, pattern)
        print('auto: Straight line')
        
    elif input_string == "zigzag":
        pattern = "pattern2"
        get_pattern(1, pattern)
        print('auto: Zigzag')
        
    elif input_string == "circle":
        pattern = "pattern4"
        get_pattern(1, pattern)
        print('auto: Circle')

    elif input_string =="sound":
        input_string="sound"
        print('sound')
        play_sound()

    elif input_string =="off":
        input_string="off"
        s.close()

    elif input_string == "laser_power":
        input_string = "laser_power"
        set_laser_power()
        
    else :
        input_string = input_string+"dont have"
        
    return call_str



def get_pattern(index, pattern):
    multi_strs = ["", ""]
    
    db = MySQLdb.connect(host="127.0.0.1", port=3306, user="root", passwd="catmint", db="catmint")
    db.set_character_set('utf8mb4')

    cursor = db.cursor()

    #sql = "SELECT pattern"+str(random.randint(1,4))+" from basicpattern"
    sql = "SELECT " + pattern + "FROM basicpattern WHERE num = " + str(index)
    
    print(sql)

    try:
        cursor.execute(sql)
        db.commit()
        rows = cursor.fetchone()
        datas = rows[0].split(" ")

        for i in range(0, len(datas)):
            print(datas[i])     # for test
            
            if datas[i].find(",") != -1:
                data=datas[i].split(",")
                
                for j in range(0, len(data)):
                    multi_strs[j] = match_direction(data[j])
                call(multi_strs[0]+" | "+multi_strs[1], shell= True)
                time.sleep(0.3)
                
            else:
                result = match_direction(datas[i])
                call(result, shell= True)
                
        db.close()
        
    except Exception as e:
        print(str(e))
        db.rollback()

        db.close()

def set_laser_power():
    global laser_power
    
    GPIO.setmode(GPIO.BCM)

    print('Setup LED pins as outputs')
    
    GPIO.setup(16, GPIO.OUT)
    GPIO.output(16, False)
    GPIO.setup(20, GPIO.OUT)
    GPIO.output(20, False)
    if laser_power == False:
        laser_power = True
        GPIO.output(16, True)
        GPIO.output(20, True)

    elif laser_power == True:
        laser_power = False
        GPIO.output(16, False)
        GPIO.output(20, False)
        GPIO.cleanup()



def play_sound():
    global sound_power
    #if sound_power==0:
        #call("bluetoothctl", shell=True)
        #call("pair 88:C6:26:49:9D:73", shell=True)
        #call("trust 88:C6:26:49:9D:73", shell=True)
        #call("connect 88:C6:26:49:9D:73", shell=True)
        #time.sleep(3)
        #call("exit", shell=True)
        #call("pacmd set-default-sink 1", shell=True)
        #call("pacmd set-default-sink bluez_sink.88_C6_26_49_9D_73", shell=True)
        #sound_power=1
        
    call("aplay /usr/share/sounds/alsa/Side_Right.wav", shell=True)

        
while True :
    conn, addr = s.accept()
    print("Connected by ", addr)

    data = conn.recv(1024)
    data = data.decode("utf8").strip()
    if not data : break
    print("Received: "+data)

    live_str = match_direction(data)
    if live_str != "":
        call(live_str, shell= True)
        time.sleep(0.3)
    conn.sendall(live_str.encode("utf-8"))      # send to android

    conn.close()
    
s.close()
                    
