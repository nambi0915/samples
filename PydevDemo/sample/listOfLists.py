'''
Created on Jan 2, 2019

@author: Nambi
'''
import re
import csv

lists=[['Server:  Corp', 'Address:  10.17.2.5\r', '\r', 'Name:    b.resolvers.level3.net\r', 'Address:  4.2.2.2\r', '\r', ''],['Server:  Corp', 'Address:  10.17.2.5\r', '\r', 'Name:    google-public-dns-a.google.com\r', 'Address:  8.8.8.8\r', '\r', ''],
['Server:  Corp', 'Address:  10.17.2.5\r', '\r', 'Name:    dns.quad9.net\r', 'Address:  9.9.9.9\r', '\r', '']]

list2=[]
for singleList in lists:
    list1=[]
    flag=0    
   
    for item in singleList:
        matcher=re.search('Name:(.*)', item)
        if matcher:
            list1.append(matcher.group(1).strip())
            print(matcher.group(1).strip())
            flag=1
            
        if flag==1:
            matcher=re.search('Address:(.*)', item)
            if matcher:
                list1.append(matcher.group(1).strip())
                print(matcher.group(1).strip())
        
    list2.append(list1)
        
print(list2)

newFile = open('export.csv','w',newline='')
newWriter = csv.writer(newFile, dialect='excel')


for i in list2:
    newWriter.writerow(i);

newFile.close();

import xlwt
from tempfile import TemporaryFile
book = xlwt.Workbook()
sheet1 = book.add_sheet('sheet1')


for j,a in enumerate(list2):
    for i,e in enumerate(a):
        sheet1.write(j,i,e)

name = "random.xls"
book.save(name)
book.save(TemporaryFile())