import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CreatePDF';

  dataset: Details = {
    name:'',
    age:null,
    country:''
  };

  constructor(private https: HttpClient) { }

  onSubmit()
  {
    this.https.post<Details>('http://localhost:8080/testapp/getdetails', this.dataset).subscribe(
      res => {
        this.dataset = res;
        console.log(this.dataset);
        alert('PDF created successfully');
        this.dataset.age = null;
        this.dataset.name = '';
        this.dataset.country = '';
      }
    );
  }
}

interface Details
{
  name:string;
  age:number;
  country:string;
}
