import pandas as pd
import re
import csv
import sqlalchemy
from sqlalchemy import create_engine


def read_tension(file):
	'''reads the csv file exported by Seal 408 with Streamer tension data'''
	d = ''
	s = 0

	with open(file) as csv_file:
		csv_reader = csv.reader(csv_file, delimiter=':')
		line_count = 0
		for row in csv_reader:
			if line_count == 0:
				d = row[1].strip()
			if line_count == 1:
				s = int(row[1].strip())
			if line_count >= 2:
				break
			line_count += 1

	data = pd.read_csv(file, sep='\t', skiprows=2)
	data.rename(columns={'    Time': 'time',
						 'Tension (daN)': 'tension',
						 'Head Buoy Power': 'hpower',
						 'Head Buoy Current (mA)': 'hcurrent',
						 'Output Voltage (V)': 'voltage'}, inplace=True)

	data['date_'] = d
	data['str'] = s
	data['date_'] = data['date_'] + ' ' + data['time']
	data['date_'] = data['date_'].astype('datetime64[ns]')
	data = data.drop('time', 1)

	return data


def read_instest(file):
	'''reads the csv file exported by Seal 408 with Instrument tests data'''
	br_line = []

	with open(file, 'r', encoding='cp1252') as file1:
		line = file1.readline()
		cnt = 0
		while line:
			pattern = re.compile(r"(^-------------------------------------------------?)")
			cnt += 1
			if pattern.search(line) is not None:
				br_line.append(cnt)
			line = file1.readline()

	if 116 in br_line:
		start = br_line[7]
		end = br_line[8]
	else:
		start = br_line[4]
		end = br_line[5]

	data = []
	cntt = 0
	with open(file, 'r', encoding='cp1252') as file1:
		line = file1.readline()
		while line:
			if cntt > start - 1 and cntt < end - 1:
				tmp = line.strip().split(' ')
				while ('' in tmp):
					tmp.remove('')
				data.append(tmp)
			cntt += 1
			line = file1.readline()

	df = pd.DataFrame(data)
	df = df.drop(3, 1)
	df['updated'] = df[[10, 11, 12, 13]].apply(lambda x: ' '.join(x), axis=1)
	df = df.drop(10, 1)
	df = df.drop(11, 1)
	df = df.drop(12, 1)
	df = df.drop(13, 1)
	df.rename(columns={0: 'ass_sn', 1: 'trace', 2: 'streamer', 4: 'fdu_sn', 5: 'type', 6: 'cap', 7: 'cutoff',
					   8: 'noise', 9: 'leakage'}, inplace=True)  # rename columns
	df['updated'] = pd.to_datetime(df['updated'], format='%b %d, %Y %H:%M')
	df = df[df.cap != 'N/A']
	df['cap'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df['cutoff'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df['noise'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df = df[df.noise != 'N/A']
	df['leakage'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df['section_rank'] = None
	df['ch_nb'] = None
	df['failure'] = ''

	return df


def read_instest2(file):
	'''
	reads the csv file exported by Seal 408 with Instrument tests data
	'''
	start = 0
	data = []
	with open(file, 'r', encoding='cp1252') as file1:
		line = file1.readline()
		cnt = 0
		while line:
			if 'ALL SENSORS    Nb' in line:
				start =+ 1
			if start >= 1 and start <= 7:
				start += 1

			if start >= 7:
				if '--' in line:
					start = 0
				else:
					start += 1
					tmp = line.strip().split(' ')
					while ('' in tmp):
						tmp.remove('')
					data.append(tmp)
			line = file1.readline()

	df = pd.DataFrame(data)
	df = df.drop(3, 1)
	df['updated'] = df[[10, 11, 12, 13]].apply(lambda x: ' '.join(x), axis=1)
	df = df.drop(10, 1)
	df = df.drop(11, 1)
	df = df.drop(12, 1)
	df = df.drop(13, 1)
	df.rename(columns={0: 'ass_sn', 1: 'trace', 2: 'streamer', 4: 'fdu_sn', 5: 'type', 6: 'cap', 7: 'cutoff',
					   8: 'noise', 9: 'leakage'}, inplace=True)  # rename columns
	df['updated'] = pd.to_datetime(df['updated'], format='%b %d, %Y %H:%M')
	df = df[df.cap != 'N/A']
	df['cap'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df['cutoff'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df['noise'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df = df[df.noise != 'N/A']
	df['leakage'].replace(regex=True,inplace=True,to_replace=r'\*', value=r'')
	df['section_rank'] = 0
	df['ch_nb'] = 0
	df['failure'] = ''

	return df
