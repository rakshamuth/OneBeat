from django.db import models

class User(models.Model):
	userId = models.CharField(max_length=200)
	userId.primary_key = True
	name = models.CharField(max_length=100)

	def __str__(self):
		return self.name

class Song(models.Model):
	artist = models.CharField(max_length=100)
	title = models.CharField(max_length=200)
	duration = models.FloatField()
	spotifyRef = models.CharField(max_length=100)

	def __str__(self):
		return self.title

class Room(models.Model):
	creator = models.ForeignKey(User)
	name = models.CharField(max_length=100)
	password = models.CharField(max_length=100,blank=True)

	def __str__(self):
		return self.name

class Playlist(models.Model):
	room = models.ForeignKey(Room)
	song = models.ForeignKey(Song)
	addedBy = models.ForeignKey(User)

class Member(models.Model):
	user = models.ForeignKey(User)
	room = models.ForeignKey(Room)