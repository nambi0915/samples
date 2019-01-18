'''
Created on Jan 2, 2019

@author: Nambi
'''
import xlwt
from tempfile import TemporaryFile
book = xlwt.Workbook()
sheet1 = book.add_sheet('sheet1')

superD =[['Server:  Corp', 'Address:  10.17.2.5\r', '\r', 'Name:    b.resolvers.level3.net\r', 'Address:  4.2.2.2\r', '\r', ''],['Server:  Corp', 'Address:  10.17.2.5\r', '\r', 'Name:    google-public-dns-a.google.com\r', 'Address:  8.8.8.8\r', '\r', ''],
['Server:  Corp', 'Address:  10.17.2.5\r', '\r', 'Name:    dns.quad9.net\r', 'Address:  9.9.9.9\r', '\r', '']]

supersecretdata = [34,123,4,1234,12,34,12,41,234,123,4,123,1,45123,5,43,61,3,56]

for j,a in enumerate(superD):
    for i,e in enumerate(a):
        sheet1.write(j,i,e)

name = "random.xls"
book.save(name)
book.save(TemporaryFile())