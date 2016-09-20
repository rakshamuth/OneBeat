from django.conf.urls import include, url
from django.contrib import admin

urlpatterns = [
	url(r'^rest-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^', include('onebeat.urls')),
]
