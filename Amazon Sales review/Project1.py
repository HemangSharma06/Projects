import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import plotly.express as px
from wordcloud import WordCloud

data = pd.read_csv("amazon.csv")

print(data.head())

top_products = data.groupby('product_name')['review_id'].count().sort_values(ascending=False).head(10)
print(top_products)
print(data.dtypes)

data['discount_percentage'] = data['discount_percentage'].str.rstrip('%')
data['discount_percentage'] = pd.to_numeric(data['discount_percentage'], errors='coerce')
data['discount_percentage'].fillna(0, inplace=True)

data['rating'] = pd.to_numeric(data['rating'], errors='coerce')
data['rating'].fillna(data['rating'].mean(), inplace=True)

fig, axes = plt.subplots(1, 2, figsize=(16, 6))

top_products.plot(kind="bar", color="red", ax=axes[0])
axes[0].set_title("Top 10 Most Reviewed Products")
axes[0].set_xlabel("Product Name")
axes[0].set_ylabel("Number of Reviews")
axes[0].tick_params(axis='x', rotation=45)
axes[0].set_xticklabels(top_products.index, ha="right")

all_reviews = ' '.join(data['review_content'].dropna())
wordcloud = WordCloud(width=800, height=400, background_color='white').generate(all_reviews)

axes[1].imshow(wordcloud, interpolation='bilinear')
axes[1].set_title('Word Cloud of Review Content')
axes[1].axis('off')

plt.tight_layout()
plt.show()

fig = px.bar(top_products,
             x=top_products.index,
             y=top_products.values,
             labels={'x': 'Product Name', 'y': 'Number of Reviews'},
             title='Top 10 Most Reviewed Products')

fig.update_layout(
    xaxis=dict(
        tickangle=-45,
        tickmode='array',
        tickvals=list(range(len(top_products.index))),
        ticktext=[name[:30] + '...' if len(name) > 30 else name for name in top_products.index]
    ),
    width=1200,
    height=600
)

fig.show()

summary = {
    'Total Products': data['product_id'].nunique(),
    'Total Reviews': data['review_id'].nunique(),
    'Average Discount (%)': round(data['discount_percentage'].mean(), 2),
    'Average Rating': round(data['rating'].mean(), 2)
}
print(pd.DataFrame(summary, index=['Summary']))

data.to_csv('processed_ecommerce_data.csv', index=False)
print("Processed data saved as 'processed_ecommerce_data.csv'")
